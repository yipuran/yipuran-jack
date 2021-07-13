package org.yipuran.json.modules;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * SqlTimestampSerializer
 */
/**
 * java.sql.Timestamp シリアライザ.
 * <PRE>
 * コンストラクタで、DateTimeFormatter を指定する
 * </PRE>
 */
public class SqlTimestampSerializer extends StdSerializer<Timestamp>{
	private DateTimeFormatter formatter;

	/**
	 * コンストラクタ.
	 * @param formatter DateTimeFormatter
	 */
	public SqlTimestampSerializer(DateTimeFormatter formatter) {
		super(Timestamp.class);
		this.formatter = formatter;
	}
	@Override
	public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider provider) throws IOException{
		gen.writeString(formatter.format(value.toLocalDateTime()));
	}
}
