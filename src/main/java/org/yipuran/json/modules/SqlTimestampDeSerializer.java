package org.yipuran.json.modules;


import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
/**
 * 時刻文字列→java.sql.Timestamp デシリアライザ.
 * <PRE>
 * コンストラクタで、DateTimeFormatter を指定することで JSON 対象文字列を Timestamp に変換する
 * </PRE>
 */
public class SqlTimestampDeSerializer extends JsonDeserializer<Timestamp>{
	private DateTimeFormatter formatter;

	/**
	 * コンストラクタ.
	 * @param formatter DateTimeFormatter
	 */
	public SqlTimestampDeSerializer(DateTimeFormatter formatter){
		 this.formatter = formatter;
	}
	@Override
	public Timestamp deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException{
		return Timestamp.valueOf(LocalDateTime.parse(p.getText(), formatter));
	}
}
