package org.yipuran.json.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.yipuran.json.JsonNodeParse;
import org.yipuran.json.test.data.Item;
import org.yipuran.json.test.tool.IResources;
import org.yipuran.json.test.tool.TriConsumer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * JsonNodeParseTest
 */
public class JsonNodeParseTest{
	private Map<String, String> valueExp;
	private Map<String, String> typeExp;
	private TriConsumer<String, String, Type> setExpectedMap = (k,v,t)->{
		valueExp.put(k, v);
		typeExp.put(k, t.getTypeName());
	};

	@Before
	public void setUp() throws Exception{
		valueExp = new HashMap<>();
		typeExp = new HashMap<>();
		setExpectedMap.accept("id", "1203", Integer.class);
		setExpectedMap.accept("name", "Sample Name", String.class);
		setExpectedMap.accept("subtitle", "null", String.class);
		setExpectedMap.accept("flg", "true", Boolean.class);
		setExpectedMap.accept("length", "0.32", Double.class);
		setExpectedMap.accept("vary[0]", "10", Integer.class);
		setExpectedMap.accept("vary[1]", "11", Integer.class);
		setExpectedMap.accept("vary[2]", "12", Integer.class);
		setExpectedMap.accept("bizDate", "2021-07-09", String.class);
		setExpectedMap.accept("bizDateTime", "2021/07/09 11:28:04", String.class);
		setExpectedMap.accept("bizTime", "17:26:45", String.class);
		setExpectedMap.accept("container.body.a", "A001", String.class);
		setExpectedMap.accept("container.body.nest.b", "12004564500023", Long.class);
		setExpectedMap.accept("group[0].name", "Red", String.class);
		setExpectedMap.accept("group[0].length", "192", Integer.class);
		setExpectedMap.accept("group[1].name", "Yellow", String.class);
		setExpectedMap.accept("group[1].length", "26", Integer.class);
		setExpectedMap.accept("group[2].name", "Green", String.class);
		setExpectedMap.accept("group[2].length", "32", Integer.class);
	}

	@Test
	public void readJson1() {
		String jsonstr = IResources.readResource("sample.json");
		JsonNodeParse jp = new JsonNodeParse();
		jp.readJson(jsonstr, (s, o)->{
			if (valueExp.containsKey(s)) {
				if (o != null) {
					assertEquals(valueExp.get(s), o.toString());
					assertEquals(typeExp.get(s), o.getClass().getName());
				}else{
					assertEquals(valueExp.get(s), "null");
				}
			}else{
				fail("Json Path Mismatch");
			}
		});
	}
	@Test
	public void readJson2() {
		try(InputStream in = IResources.readResourceStream("sample.json")){
			JsonNodeParse jp = new JsonNodeParse();
			jp.readJson(in, (s, o)->{
				if (valueExp.containsKey(s)) {
					if (o != null) {
						assertEquals(valueExp.get(s), o.toString());
						assertEquals(typeExp.get(s), o.getClass().getName());
					}else{
						assertEquals(valueExp.get(s), "null");
					}
				}else{
					fail("Json Path Mismatch");
				}
			});
		}catch(IOException e){
			fail("test file not found!");
		}
	}
	@Test
	public void readJson3() {
		String jsonstr = IResources.readResource("sample2.json");
		JsonNodeParse jp = new JsonNodeParse();
		JsonNode node = jp.readTree(jsonstr);
		jp.readJson(node, (s, o)->{

			if (s.equals("bizDate")) {
				assertEquals("2021-07-09", o.toString());
			}
			if (s.equals("bizDateTime")) {
				assertEquals("2021/07/09 11:28:04", o.toString());
			}
			if (s.equals("bizTime")) {
				assertEquals("17:26:45", o.toString());
			}
		});
	}

	@Test
	public void jsonStream1() {
		String jsonstr = IResources.readResource("sample.json");
		JsonNodeParse jp = new JsonNodeParse();
		Map<String, String> res = 	jp.stream(jsonstr)
				.collect(HashMap::new, (r, t)->	r.put(t.getKey(), Optional.ofNullable(t.getValue()).map(Object::toString).orElse("null")), (r, t)->{});
		assertEquals(valueExp, res);
	}
	@Test
	public void jsonStream2() {
		try(InputStream in = IResources.readResourceStream("sample.json")){
			JsonNodeParse jp = new JsonNodeParse();
			Map<String, String> res = 	jp.stream(in)
					.collect(HashMap::new, (r, t)->	r.put(t.getKey(), Optional.ofNullable(t.getValue()).map(Object::toString).orElse("null")), (r, t)->{});
			assertEquals(valueExp, res);
		}catch(IOException e){
			fail("test file not found!");
		}
	}
	@Test
	public void readTree(){
		try(InputStream in = IResources.readResourceStream("sample.json")){
			String jsonstr = IResources.readResource("sample.json");
			JsonNodeParse jp = new JsonNodeParse();
			JsonNode node = jp.readTree(jsonstr);
			assertEquals(node, jp.readTree(in));
		}catch(IOException e){
			fail("test file not found!");
		}
	}
	@Test
	public void eachTest() {
		String jsonstr = "{\"a\":12,\"b\":[1,2,3]}";
		JsonNodeParse jp = new JsonNodeParse();
		JsonNode node = jp.readTree(jsonstr);
		jp.each(node, (s, j)->{
			if (s.equals("a")) {
				assertEquals(12, j.findPath("a").asLong());
			}
			if (s.equals("b")) {
				assertEquals("[1,2,3]", j.findPath("b").toString());
			}
		});
	}
	@Test
	public void nodestream() {
		String jsonstr = "{\"a\":12,\"b\":[1,2,3]}";
		JsonNodeParse jp = new JsonNodeParse();
		JsonNode node = jp.readTree(jsonstr);
		jp.nodeStream(node).forEach(e->{
			String k = e.getKey();
			if (k.equals("a")) {
				assertEquals(12, e.getValue().findPath("a").asLong());
			}
			if (k.equals("b")) {
				assertEquals("[1,2,3]", e.getValue().findPath("b").toString());
			}
		});
	}
	@Test
	public void streamText() {
		String jsonstr = "{\"a\":12,\"b\":[1,2,3]}";
		JsonNodeParse jp = new JsonNodeParse();
		jp.stream(jsonstr).forEach(e->{
			String k = e.getKey();
			if (k.equals("a")) {
				assertEquals(12, e.getValue());
			}
			if (k.equals("b[0]")) {
				assertEquals(1, e.getValue());
			}
			if (k.equals("b[1]")) {
				assertEquals(2, e.getValue());
			}
			if (k.equals("b[2]")) {
				assertEquals(3, e.getValue());
			}
		});
	}
	@Test
	public void jsonStream() {
		String jsonstr = "{\"a\":12,\"b\":[1,2,3]}";
		JsonNodeParse jp = new JsonNodeParse();
		JsonNode node = jp.readTree(jsonstr);

		jp.stream(node).forEach(e->{
			String k = e.getKey();
			if (k.equals("a")) {
				assertEquals(12, e.getValue());
			}
			if (k.equals("b[0]")) {
				assertEquals(1, e.getValue());
			}
			if (k.equals("b[1]")) {
				assertEquals(2, e.getValue());
			}
			if (k.equals("b[2]")) {
				assertEquals(3, e.getValue());
			}
		});
	}
	@Test
	public void streamInput() {
		try(InputStream in = IResources.readResourceStream("sample2.json")){
			JsonNodeParse jp = new JsonNodeParse();
			jp.stream(in).forEach(e->{
				String k = e.getKey();
				if (k.equals("bizDate")) {
					assertEquals("2021-07-09", e.getValue());
				}
				if (k.equals("bizDateTime")) {
					assertEquals("2021/07/09 11:28:04", e.getValue());
				}
				if (k.equals("bizTime")) {
					assertEquals("17:26:45", e.getValue());
				}
			});
		}catch(IOException e){
			fail("test file not found!");
		}
	}
	@Test
	public void addDeserilaize() {
		try(InputStream in = IResources.readResourceStream("sample2.json")){
			JsonNodeParse jp = new JsonNodeParse();
			jp = jp.addDeserilaize(Pattern.compile("^bizDate$"), j->LocalDate.parse(j.asText()));
			jp = jp.addDeserilaize(Pattern.compile("^bizDateTime$"), j->LocalDateTime.parse(j.asText(), DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
			jp = jp.addDeserilaize(Pattern.compile("^bizTime$"), j->LocalTime.parse(j.asText(), DateTimeFormatter.ofPattern("HH:mm:ss")));
			jp.stream(in).forEach(e->{
				String k = e.getKey();
				if (k.equals("bizDate")) {
					assertEquals(LocalDate.of(2021, 7, 9), e.getValue());
				}
				if (k.equals("bizDateTime")) {
					assertEquals(LocalDateTime.of(2021, 7, 9, 11, 28, 4), e.getValue());
				}
				if (k.equals("bizTime")) {
					assertEquals(LocalTime.of(17, 26, 45), e.getValue());
				}
			});
		}catch(IOException e){
			fail("test file not found!");
		}
	}
	@Test
	public void toFunction() {
		String jsonstr = IResources.readResource("sample.json");
		JsonNodeParse jp = new JsonNodeParse();
		JsonNode node = jp.readTree(jsonstr);
		jp = jp.addDeserilaize(Pattern.compile("group\\[\\d+\\]"), JsonNodeParse.toFunction(Item.class));
		jp.readJson(node, (k, o)->{
			if (k.equals("group[0]")) {
				assertEquals(new Item("Red", 192).toString(), o.toString());
			}
			if (k.equals("group[1]")) {
				assertEquals(new Item("Yellow", 26).toString(), o.toString());
			}
			if (k.equals("group[2]")) {
				assertEquals(new Item("Green", 32).toString(), o.toString());
			}
		});
	}
	@Test
	public void failed() {
		String jsonstr = "{\"a\":12,\"b\":[1,2,3],}";
		JsonNodeParse jp = new JsonNodeParse();
		Throwable ex = assertThrows(Throwable.class, ()->{
			jp.readTree(jsonstr);
		});
		assertEquals(JsonParseException.class, ex.getCause().getClass());
	}
}
