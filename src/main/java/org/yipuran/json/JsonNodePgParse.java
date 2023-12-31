package org.yipuran.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

/**
 * PostgreSQL JSON演算子パス生成用 JSON Parser
 * <pre>
 * JsonNodeParse の継承クラス
 * PostgreSQL JSON演算子パスの表現、
 *    'A'->'B'->>'C'  : C の値が、 文字列、数値、null
 *    'A'->'B'->'C'   : C の値が、JSonNode
 *    'A'->'D'->>2    : 配列D インデックス=2 の値
 *    'A'->'D'->2     : 配列D インデックス=2 のJSonNode
 * 上記の表現形式の Entry＜String, JsonNode＞ のStreamを生成する目的のクラス
 * </pre>
 */
public class JsonNodePgParse extends JsonNodeParse{

	/**
	 * JsonNode Entry Stream生成.
	 * <pre>
	 * Entryのkeyは、PostgreSQL の JSON演算子 -> ->> とシングルクォート括りによるパス
	 * value がJSONオブジェクトであれば、"->"
	 * value が文字列、数値、null であれば、最後が、"->>"
	 * </pre>
	 * @param node JsonNode
	 * @return Entry＜String, JsonNode＞ の Stream
	 */
	@Override
	public Stream<Entry<String, JsonNode>> nodeStream(JsonNode node){
		Map<String, JsonNode> map = new HashMap<>();
		nodeJsonPg(node, "", map);
		return map.entrySet().stream();
	}

	private void nodeJsonPg(JsonNode node, String path, Map<String, JsonNode> map) {
		if (node.getNodeType().equals(JsonNodeType.OBJECT)) {
			for(Iterator<Entry<String, JsonNode>> it=node.fields(); it.hasNext();) {
				Entry<String, JsonNode> entry = it.next();
				String _path = path + "->" + "'" + entry.getKey() + "'";
				map.put(_path.substring(2), node);
				nodeJsonPg(entry.getValue(), path + "->" + "'"+ entry.getKey() + "'", map);
			}
		}else if(node.getNodeType().equals(JsonNodeType.ARRAY)){
			if (node.size() > 0){
				int x=0;
				for(Iterator<JsonNode> it=node.iterator(); it.hasNext();x++){
					nodeJsonPg(it.next(), path + "->" + x, map);
				}
			}
		}else if(node.getNodeType().equals(JsonNodeType.NULL)){
			map.put(relaceLastallow(path.substring(2)), null);
		}else{
			map.put(relaceLastallow(path.substring(2)), node);
		}
	}
	private Pattern lastallow = Pattern.compile("\\->(?<=\\->)([^\\->]+$)");
	private String relaceLastallow(String key) {
		Matcher m = lastallow.matcher(key);
		if (m.find()) {
			return key.substring(0, m.start()) + m.group().replaceFirst("\\->", "->>");

		}
		return key;
	}
}
