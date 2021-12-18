package org.yipuran.json.util;

import org.yipuran.json.databind.UpperSnakeCaseStrategy;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
/**
 * Jacksonユーティリティ.
 * @since 1.2
 */
public final class JackUtil {
	private JackUtil(){}

	/**
	 * ２文字目大文字でも UnrecognizedPropertyException を起こさない ObjectMapperを生成.
	 * <pre>
	 * PropertyAccessor.ALL, Visibility.NONE, PropertyAccessor.FIELD, Visibility.ANY を設定して
	 * UnrecognizedPropertyException 回避している。
	 * </pre>
	 * @return ObjectMapper
	 */
	public static ObjectMapper getObjectMapper() {
		return ReturnObjectMapper.of()
		.with(e->e.setVisibility(PropertyAccessor.ALL, Visibility.NONE))
		.with(e->e.setVisibility(PropertyAccessor.FIELD, Visibility.ANY))
		.apply(new ObjectMapper());
	}
	/**
	 * SnakeCase解析用 ObjectMapper 生成.
	 * <pre>
	 * PropertyAccessor.ALL, Visibility.NONE, PropertyAccessor.FIELD, Visibility.ANY を設定して
	 * UnrecognizedPropertyException 回避している。
	 * </pre>
	 * @return ObjectMapper
	 */
	public static ObjectMapper getObjectMapperWithrSnake() {
		return ReturnObjectMapper.of()
		.with(e->e.setVisibility(PropertyAccessor.ALL, Visibility.NONE))
		.with(e->e.setVisibility(PropertyAccessor.FIELD, Visibility.ANY))
		.with(e->e.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE))
		.apply(new ObjectMapper());
	}
	/**
	 * 大文字のSnakeCase解析用 ObjectMapper 生成.
	 * <pre>
	 * PropertyAccessor.ALL, Visibility.NONE, PropertyAccessor.FIELD, Visibility.ANY を設定して
	 * UnrecognizedPropertyException 回避している。
	 * </pre>
	 * @return ObjectMapper
	 */
	public static ObjectMapper getObjectMapperWithUpperSnake() {
		return ReturnObjectMapper.of()
		.with(e->e.setVisibility(PropertyAccessor.ALL, Visibility.NONE))
		.with(e->e.setVisibility(PropertyAccessor.FIELD, Visibility.ANY))
		.with(e->e.setPropertyNamingStrategy(new UpperSnakeCaseStrategy()))
		.apply(new ObjectMapper());
	}
}
