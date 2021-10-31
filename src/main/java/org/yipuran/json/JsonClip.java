package org.yipuran.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Indenter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
/**
 * JsonClip.
 * JSONをマージする為のクラス
 * <PRE>
 * マージされる元のJsonを指定したコンストラクタで生成するインスタンスで、以下のマージ実行メソッドを提供する。
 * 　update : 更新モードのマージ
 * 　updateArrayMerge : 配列維持の更新モードのマージ
 * 　merge : 同じキーがマージ先に存在する場合、上書きではなく配列として追加するマージ
 *
 * </PRE>
 * @since 1.1
 */
public final class JsonClip{
	private JsonNode basenode;

	/**
	 * JsonNode指定コンストラクタ.
	 * @param node マージされる元のJsonNode
	 */
	public JsonClip(JsonNode node){
		basenode = node;
	}
	/**
	 * JSONテキスト指定コンストラクタ.
	 * @param jsonstr マージされる元のJSON文字列
	 */
	public JsonClip(String jsonstr){
		ObjectMapper mapper = new ObjectMapper();
		try{
			basenode = mapper.readTree(jsonstr);
		}catch(JsonMappingException e){
			throw new RuntimeException(e);
		}catch(JsonProcessingException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * InputStream指定コンストラクタ.
	 * @param in マージされる元のJSONテキストのInputStream
	 */
	public JsonClip(InputStream in){
		ObjectMapper mapper = new ObjectMapper();
		try{
			basenode = mapper.readTree(in);
		}catch(JsonMappingException e){
			throw new RuntimeException(e);
		}catch(JsonProcessingException e){
			throw new RuntimeException(e);
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 更新モードのマージ.
	 * <PRE>
	 * 同じキーがマージ先に存在する場合、マージ先の値は保持されずに上書きされる
	 * </PRE>
	 * @param jsontext マージするJSONテキスト
	 * @return マージ結果JSONテキスト
	 */
	public String update(String jsontext){
		ObjectMapper mapper = new ObjectMapper();
		try{
			JsonNode node = mapper.readTree(jsontext);
			JsonNode resNode = update(basenode, node);
			DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
			Indenter indenter = new DefaultIndenter();
			printer.indentObjectsWith(indenter);
			printer.indentArraysWith(indenter);
			OutputStream bo = new ByteArrayOutputStream();
			mapper.writer(printer).writeValue(bo, resNode);
			bo.flush();
			bo.close();
			return bo.toString();
		}catch(JsonMappingException e){
			throw new RuntimeException(e);
		}catch(JsonProcessingException e){
			throw new RuntimeException(e);
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * JsonNode指定、更新モードのマージ.
	 * <PRE>
	 * 同じキーがマージ先に存在する場合、マージ先の値は保持されずに上書きされる
	 * </PRE>
	 * @param node マージするJsonNode
	 * @return マージ結果JsonNode
	 */
	public JsonNode update(JsonNode node){
		return update(basenode, node);
	}

	/**
	 * 配列維持の更新モードのマージ.
	 * <PRE>
	 * 同じキーがマージ先に存在する場合、マージ先の値は保持されずに上書きされるが、
	 * 配列である場合、配列に追加でマージされる。
	 * </PRE>
	 * @param jsontext マージするJSONテキスト
	 * @return マージ結果JSONテキスト
	 */
	public String updateArrayMerge(String jsontext){
		ObjectMapper mapper = new ObjectMapper();
		try{
			JsonNode node = mapper.readTree(jsontext);
			JsonNode resNode = updateArrayMerge(basenode, node);
			DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
			Indenter indenter = new DefaultIndenter();
			printer.indentObjectsWith(indenter);
			printer.indentArraysWith(indenter);
			OutputStream bo = new ByteArrayOutputStream();
			mapper.writer(printer).writeValue(bo, resNode);
			bo.flush();
			bo.close();
			return bo.toString();
		}catch(JsonMappingException e){
			throw new RuntimeException(e);
		}catch(JsonProcessingException e){
			throw new RuntimeException(e);
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 配列維持のJsonNode指定、更新モードのマージ.
	 * <PRE>
	 * 同じキーがマージ先に存在する場合、マージ先の値は保持されずに上書きされるが、
	 * 配列である場合、配列に追加でマージされる。
	 * </PRE>
	 * @param node マージするJsonNode
	 * @return マージ結果JsonNode
	 */
	public JsonNode updateArrayMerge(JsonNode node){
		return updateArrayMerge(basenode, node);
	}

	/**
	 * JSONテキストマージ.
	 * <PRE>
	 * 同じキーがマージ先に存在する場合、上書きではなく配列として追加するマージが実行される。
	 * マージ先が配列ではない場合で同じキーのマージは、配列に変更されることに注意。
	 * </PRE>
	 * @param jsontext マージするJSONテキスト
	 * @return マージ結果JSONテキスト
	 */
	public String merge(String jsontext){
		ObjectMapper mapper = new ObjectMapper();
		try{
			JsonNode node = mapper.readTree(jsontext);
			JsonNode resNode = merge(basenode, node);
			DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
			Indenter indenter = new DefaultIndenter();
			printer.indentObjectsWith(indenter);
			printer.indentArraysWith(indenter);
			OutputStream bo = new ByteArrayOutputStream();
			mapper.writer(printer).writeValue(bo, resNode);
			bo.flush();
			bo.close();
			return bo.toString();
		}catch(JsonMappingException e){
			throw new RuntimeException(e);
		}catch(JsonProcessingException e){
			throw new RuntimeException(e);
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * JsonNodeマージ.
	 * <PRE>
	 * 同じキーがマージ先に存在する場合、上書きではなく配列として追加するマージが実行される。
	 * マージ先が配列ではない場合で同じキーのマージは、配列に変更されることに注意。
	 * </PRE>
	 * @param node マージするJsonNode
	 * @return マージ結果JsonNode
	 */
	public JsonNode merge(JsonNode node){
		return merge(basenode, node);
	}

	/**
	 * 更新モードのマージ static メソッド.
	 * <PRE>
	 * 同じキーがマージ先に存在する場合、マージ先の値は保持されずに上書きされる
	 * </PRE>
	 * @param baseNode マージ先のJsonNode
	 * @param upNode マージするJsonNode
	 * @return マージ結果JsonNode
	 */
	public static JsonNode update(JsonNode baseNode, JsonNode upNode){
		ObjectNode node = JsonNodeFactory.instance.objectNode();
		for(Iterator<Entry<String, JsonNode>> it = baseNode.fields(); it.hasNext();){
			Entry<String, JsonNode> e = it.next();
			node.set(e.getKey(), e.getValue());
		}
		for(Iterator<Entry<String, JsonNode>> it = upNode.fields(); it.hasNext();){
			Entry<String, JsonNode> e = it.next();
			node.set(e.getKey(), e.getValue());
		}
	   return node;
	}
	/**
	 * 配列維持のJsonNode指定、更新モードのマージ static メソッド.
	 * <PRE>
	 * 同じキーがマージ先に存在する場合、マージ先の値は保持されずに上書きされるが、
	 * 配列である場合、配列に追加でマージされる。
	 * </PRE>
	 * @param baseNode マージ先のJsonNode
	 * @param upNode マージするJsonNode
	 * @return マージ結果JsonNode
	 */
	public static JsonNode updateArrayMerge(JsonNode baseNode, JsonNode upNode){
		ObjectNode node = JsonNodeFactory.instance.objectNode();
		Map<String, Integer> map = new HashMap<>();
		for(Iterator<String> it = baseNode.fieldNames(); it.hasNext();){
			map.put(it.next(), 1);
		}
		for(Iterator<String> it = upNode.fieldNames(); it.hasNext();){
			map.put(it.next(), 2);
		}
		map.entrySet().stream().forEach(e->{
			String key = e.getKey();
			if (e.getValue()==1){
				node.set(e.getKey(), baseNode.get(key));
			}else{
				JsonNode n = baseNode.get(key);
				if (n==null){
					node.set(key, upNode.get(key));
				}else{
					JsonNode n1 = baseNode.get(key);
					JsonNode n2 = upNode.get(key);
					if (n1.isArray() && n2.isArray()){
						ArrayNode array = JsonNodeFactory.instance.arrayNode();
						for(Iterator<JsonNode> it = n1.elements(); it.hasNext();){
							array.add(it.next());
						}
						for(Iterator<JsonNode> it = n2.elements(); it.hasNext();){
							array.add(it.next());
						}
						node.set(key, array);
					}else if(n1.isObject() && n2.isObject()){
						node.set(key, updateArrayMerge(n1, n2));
					}else{
						node.set(key, n2);
					}
				}
			}
		});
	   return node;
	}
	/**
	 * JsonNodeマージ.
	 * <PRE>
	 * 同じキーがマージ先に存在する場合、上書きではなく配列として追加するマージが実行される。
	 * マージ先が配列ではない場合で同じキーのマージは、配列に変更されることに注意。
	 * </PRE>
	 * @param baseNode マージ先のJsonNode
	 * @param upNode マージするJsonNode
	 * @return マージ結果JsonNode
	 */
	public static JsonNode merge(JsonNode baseNode, JsonNode upNode) {
		ObjectNode node = JsonNodeFactory.instance.objectNode();
		Map<String, Boolean> map = new HashMap<>();
		for(Iterator<String> it = baseNode.fieldNames(); it.hasNext();){
			map.put(it.next(), false);
		}
		for(Iterator<String> it = upNode.fieldNames(); it.hasNext();){
			String s = it.next();
			map.put(s, map.get(s)==null ? false : true);
		}
		map.entrySet().stream().forEach(e->{
			String key = e.getKey();
			if (e.getValue()){
				JsonNode n = baseNode.get(key);
				if (n==null){
					node.set(key, upNode.get(key));
				}else{
					JsonNode n1 = baseNode.get(key);
					JsonNode n2 = upNode.get(key);
					ArrayNode array = JsonNodeFactory.instance.arrayNode();
					if (n1.isArray()) {
						for(Iterator<JsonNode> it = n1.elements(); it.hasNext();){
							array.add(it.next());
						}
					}else{
						array.add(n1);
					}
					if (n2.isArray()) {
						for(Iterator<JsonNode> it = n2.elements(); it.hasNext();){
							array.add(it.next());
						}
					}else{
						array.add(n2);
					}
					node.set(key, array);
				}
			}else{
				node.set(key, upNode.get(key));
			}
		});
	   return node;
	}
	/**
	 * JsonNode ⇒ JSONテキスト.
	 * @param node JsonNode
	 * @return JSONテキスト
	 */
	public static String prettyPrint(JsonNode node){
		ObjectMapper mapper = new ObjectMapper();
		try{
			DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
			Indenter indenter = new DefaultIndenter();
			printer.indentObjectsWith(indenter);
			printer.indentArraysWith(indenter);
			OutputStream bo = new ByteArrayOutputStream();
			mapper.writer(printer).writeValue(bo, node);
			bo.flush();
			bo.close();
			return bo.toString();
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * インデント指定 JsonNode ⇒ JSONテキスト.
	 * @param node JsonNode
	 * @param indent インデント
	 * @param eol 要素区切り文字列、"\n" 等、JSON書式を破壊しない文字列を指定する
	 * @return JSONテキスト
	 */
	public static String prettyPrint(JsonNode node, String indent, String eol){
		ObjectMapper mapper = new ObjectMapper();
		try{
			DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
			Indenter indenter = new DefaultIndenter(indent, eol);
			printer.indentObjectsWith(indenter);
			printer.indentArraysWith(indenter);
			OutputStream bo = new ByteArrayOutputStream();
			mapper.writer(printer).writeValue(bo, node);
			bo.flush();
			bo.close();
			return bo.toString();
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
}
