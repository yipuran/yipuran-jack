package org.yipuran.json.modules;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * エポック時間 java.sql.Timestamp デシリアライザ.
 * <PRE>
 * エポックミリ秒をデシリアライズする。
 * JSON 値がエポック秒 の場合、1000を積算してデシリアライズする。
 *
 * JSON対象の値が数値でない場合、JsonProcessingException をthrowする
 * </PRE>
 */
public class EpochSqlTimestampDeserializer extends JsonDeserializer<Timestamp>{
	@Override
	public Timestamp deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException{
		if (!p.currentToken().isNumeric()) {
			throw new JsonProcessingException(p.currentName() + " is not Numeric"){};
		}
		long n = p.getValueAsLong();
		if (n < 1000000000000L) n = n * 1000;
		return Timestamp.valueOf(	Instant.ofEpochMilli(n).atZone(ZoneId.systemDefault()).toLocalDateTime());
	}
}
