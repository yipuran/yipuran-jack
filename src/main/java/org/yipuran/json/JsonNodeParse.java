package org.yipuran.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

/**
 * JSON読込み JsonNode 解析.
 * <PRE>
 * JSON を読込み、JsonNode or key-value の Object 読み出しを処理する。
 * </PRE>
 */
public class JsonNodeParse{
	private ObjectMapper mapper;
	private Map<String, Function<JsonNode, Object>> dMap;

	/**
	 * コンストラクタ.
	 */
	public JsonNodeParse() {
		dMap = new HashMap<>();
		mapper = new ObjectMapper();
	}
	/**
	 * ObjectMapper指定コンストラクタ.
	 * @param mapper ObjectMapper
	 */
	public JsonNodeParse(ObjectMapper mapper) {
		dMap = new HashMap<>();
		this.mapper = mapper;
	}

	/**
	 * JSONキー正規表現一致に対するデシリアライザの登録.
	 * <PRE>
	 * キーの書式
	 *   ・階層は、"." 文字で区切り　"." は、正規表現でエスケープの必要有り
	 *   ・配列は、[n] n=０始まり　　[] は、正規表現でエスケープの必要有り
	 * </PRE>
	 * @param ptn Pattern（正規表現）
	 * @param deserializer デシリアライザ
	 * @return JsonNodeParse
	 */
	public JsonNodeParse addDeserilaize(Pattern ptn, Function<JsonNode, Object> deserializer) {
		dMap.put(ptn.pattern(), deserializer);
		return this;
	}
	/**
	 * JSON文字列→BiConsumer.
	 * @param jsontxt JSON文字列
	 * @param biconsumer キーと読込結果Object の BiConsumer
	 */
	public void readJson(String jsontxt, BiConsumer<String, Object> biconsumer){
		try{
			parseJson(mapper.readTree(jsontxt), "", biconsumer);
		}catch (JsonProcessingException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * InputStream でJSON読込→BiConsumer.
	 * @param in java.io.InputStream
	 * @param biconsumer キーと読込結果Object の BiConsumer
	 */
	public void readJson(InputStream in, BiConsumer<String, Object> biconsumer){
		try{
			parseJson(mapper.readTree(in), "", biconsumer);
		}catch (JsonProcessingException e){
			throw new RuntimeException(e);
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * JsonNode 解析→BiConsumer.
	 * @param node JsonNode
	 * @param biconsumer キーと読込結果Object の BiConsumer
	 */
	public void readJson(JsonNode node, BiConsumer<String, Object> biconsumer){
		parseJson(node, "", biconsumer);
	}

	private void parseJson(JsonNode node, String path, BiConsumer<String, Object> con){
		String p = path.length() > 0 ? path.substring(1) : path;
		Function<JsonNode, Object> nodeparser;
		if (dMap.size() > 0 && (nodeparser = dMap.entrySet().stream()
				.filter(e->Pattern.compile(e.getKey()).matcher(p).find())
				.findAny().map(e->e.getValue()).orElse(null)) != null) {
			con.accept(p, nodeparser.apply(node));
		}else{
			if (node.getNodeType().equals(JsonNodeType.OBJECT)) {
				for(Iterator<Entry<String, JsonNode>> it=node.fields(); it.hasNext();) {
					Entry<String, JsonNode> entry = it.next();
					parseJson(entry.getValue(), path + "." + entry.getKey(), con);
				}
			}else if(node.getNodeType().equals(JsonNodeType.ARRAY)){
				if (node.size() > 0){
					int x=0;
					for(Iterator<JsonNode> it=node.iterator(); it.hasNext();x++){
						parseJson(it.next(), path + "[" + x + "]", con);
					}
				}else{
					con.accept(p, new ArrayList<Object>());
				}
			}else if(node.getNodeType().equals(JsonNodeType.NULL)){
				con.accept(p, null);
			}else if(node.getNodeType().equals(JsonNodeType.NUMBER)){
				if (node.isDouble()){
					con.accept(p, node.asDouble());
				}else if(node.isLong()){
					con.accept(p, node.asLong());
				}else{
					con.accept(p, node.asInt());
				}
			}else if(node.getNodeType().equals(JsonNodeType.BOOLEAN)){
				con.accept(p, node.asBoolean());
			}else if(node.getNodeType().equals(JsonNodeType.STRING)){
				con.accept(p, node.asText());
			}
		}
	}
	/**
	 * JsonNode 解析→読込Entry のStream生成.
	 * @param node JsonNode
	 * @return キーと読込結果Object の Map.Entry の Stream
	 */
	public Stream<Entry<String, Object>> stream(JsonNode node){
		Stream.Builder<Entry<String, Object>> builder = Stream.builder();
		parseJson(node, "", builder);
		return  builder.build();
	}

	private void parseJson(JsonNode node, String path, Stream.Builder<Entry<String, Object>> builder){
		String p = path.length() > 0 ? path.substring(1) : path;
		Function<JsonNode, Object> nodeparser;
		if (dMap.size() > 0 && (nodeparser = dMap.entrySet().stream()
				.filter(e->Pattern.compile(e.getKey()).matcher(p).find())
				.findAny().map(e->e.getValue()).orElse(null)) != null) {
			builder.add(new SimpleEntry<String, Object>(p, nodeparser.apply(node)));
		}else{
			if (node.getNodeType().equals(JsonNodeType.OBJECT)) {
				for(Iterator<Entry<String, JsonNode>> it=node.fields(); it.hasNext();) {
					Entry<String, JsonNode> entry = it.next();
					parseJson(entry.getValue(), path + "." + entry.getKey(), builder);
				}
			}else if(node.getNodeType().equals(JsonNodeType.ARRAY)){
				if (node.size() > 0){
		         int x=0;
		         for(Iterator<JsonNode> it=node.iterator(); it.hasNext();x++){
		            parseJson(it.next(), path + "[" + x + "]", builder);
		         }
		      }else{
		         builder.add(new SimpleEntry<String, Object>(p, new ArrayList<Object>()));
		      }
			}else if(node.getNodeType().equals(JsonNodeType.NULL)){
				builder.add(new SimpleEntry<String, Object>(p, null));
			}else if(node.getNodeType().equals(JsonNodeType.NUMBER)){
				if (node.isDouble()){
					builder.add(new SimpleEntry<String, Object>(p, node.asDouble()));
				}else if(node.isLong()){
					builder.add(new SimpleEntry<String, Object>(p, node.asLong()));
				}else{
					builder.add(new SimpleEntry<String, Object>(p, node.asInt()));
				}
			}else if(node.getNodeType().equals(JsonNodeType.BOOLEAN)){
				builder.add(new SimpleEntry<String, Object>(p, node.asBoolean()));
			}else if(node.getNodeType().equals(JsonNodeType.STRING)){
				builder.add(new SimpleEntry<String, Object>(p, node.asText()));
			}
		}
	}
	/**
	 * JsonNode → 任意クラス生成.
	 * <PRE>
	 * ObjectMapper の readerFor を実行して JsonNode で readValue 実行して指定クラスのインスタンスを生成する。
	 * JsonNodeParse の addDeserilaize(Pattern, Function＜JsonNode, Object＞) で Function として指定するのが効果的
	 * </PRE>
	 * @param cls 生成するクラス
	 * @return Function＜JsonNode, Object＞
	 */
	public static Function<JsonNode, Object> toFunction(Class<?> cls){
		return t->{
			try{
				return new ObjectMapper().readerFor(cls).readValue(t);
			}catch(Throwable ex){
				throw new RuntimeException(ex);
			}
		};
	}
	/**
	 * JSON文字列→JsonNode取得.
	 * <PRE>ObjectMapper の readTree をラップ、例外発生をRuntimeExceptionでラップ</PRE>
	 * @param jsonstr JSON文字列
	 * @return JsonNode
	 */
	public JsonNode readTree(String jsonstr) {
		try{
			return mapper.readTree(jsonstr);
		}catch (JsonMappingException e){
			throw new RuntimeException(e);
		}catch (JsonProcessingException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * InputStream読込→JsonNode取得.
	 * <PRE>ObjectMapper の readTree をラップ、例外発生をRuntimeExceptionでラップ</PRE>
	 * @param in java.io.InputStream
	 * @return JsonNode
	 */
	public JsonNode readTree(InputStream in) {
		try{
			return mapper.readTree(in);
		}catch (JsonMappingException e){
			throw new RuntimeException(e);
		}catch (JsonProcessingException e){
			throw new RuntimeException(e);
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * JsonNode each解析 →BiConsumer.
	 * <PRE>
	 * readJson(JsonNode, BiConsumer＜String, Object＞) とは異なり
	 * each(JsonNode, BiConsumer＜String, JsonNode＞) である。
	 * </PRE>
	 * @param node JsonNode
	 * @param biconsumer キーと読込結果JsonNode の BiConsumer
	 */
	public void each(JsonNode node, BiConsumer<String, JsonNode> biconsumer) {
		eachJson(node, "", biconsumer);
	}
	private void eachJson(JsonNode node, String path, BiConsumer<String, JsonNode> consumer){
		if(node.getNodeType().equals(JsonNodeType.OBJECT)) {
			for(Iterator<Entry<String, JsonNode>> it=node.fields(); it.hasNext();) {
				Entry<String, JsonNode> entry = it.next();
				String _path = path + "." + entry.getKey();
				consumer.accept(_path.substring(1), node);
				eachJson(entry.getValue(), path + "." + entry.getKey(), consumer);
			}
		}else if(node.getNodeType().equals(JsonNodeType.ARRAY)){
			if (node.size() > 0){
				int x=0;
				for(Iterator<JsonNode> it=node.iterator(); it.hasNext();x++){
					eachJson(it.next(), path + "[" + x + "]", consumer);
				}
			}
		}
	}
	/**
	 * JsonNode Entry Stream生成.
	 * @param node JsonNode
	 * @return Entry＜String, JsonNode＞ の Stream
	 */
	public Stream<Entry<String, JsonNode>> nodeStream(JsonNode node){
		Stream.Builder<Entry<String, JsonNode>> builder = Stream.builder();
		nodeJson(node, "", builder);
		return  builder.build();
	}
	/**
	 * JSON文字列→読込Entry のStream生成.
	 * @param jsontxt JSON文字列
	 * @return キーと読込結果Object の Map.Entry の Stream
	 */
	public Stream<Entry<String, Object>> stream(String jsontxt){
		Stream.Builder<Entry<String, Object>> builder = Stream.builder();
		ObjectMapper mapper = new ObjectMapper();
		try{
			parseJson(mapper.readTree(jsontxt), "", builder);
		}catch (JsonProcessingException e){
			throw new RuntimeException(e);
		}
		return  builder.build();
	}
	/**
	 * InputStream でJSON読込→読込Entry のStream生成.
	 * @param in java.io.InputStream
	 * @return キーと読込結果Object の Map.Entry の Stream
	 */
	public Stream<Entry<String, Object>> stream(InputStream in){
		Stream.Builder<Entry<String, Object>> builder = Stream.builder();
		ObjectMapper mapper = new ObjectMapper();
		try{
			parseJson(mapper.readTree(in), "", builder);
		}catch (JsonProcessingException e){
			throw new RuntimeException(e);
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		return  builder.build();
	}
	private void nodeJson(JsonNode node, String path, Stream.Builder<Entry<String, JsonNode>> builder){
		if(node.getNodeType().equals(JsonNodeType.OBJECT)) {
			for(Iterator<Entry<String, JsonNode>> it=node.fields(); it.hasNext();) {
				Entry<String, JsonNode> entry = it.next();
				String _path = path + "." + entry.getKey();
				builder.add(new SimpleEntry<String, JsonNode>(_path.substring(1), node));
				nodeJson(entry.getValue(), path + "." + entry.getKey(), builder);
			}
		}else if(node.getNodeType().equals(JsonNodeType.ARRAY)){
			if (node.size() > 0){
				int x=0;
				for(Iterator<JsonNode> it=node.iterator(); it.hasNext();x++){
					nodeJson(it.next(), path + "[" + x + "]", builder);
				}
			}
		}
	}
}
