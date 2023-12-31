package org.yipuran.json.util;

import java.io.Serializable;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ObjectMapper に対して Consumer 処理をして、処理後のObjectMapperを返す。
 * <pre>
 * static of() から、with(Consumer)指定の下、get で渡す ObjectMapper から結果を取得する。
 * （例）
 *     ObjectMapper mapper ＝ ReturnObjectMapper.of()
 *                           .with(e->e.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE))
 *                           .get(new ObjectMapper());
 * </pre>
 * @since 1.2
 */
@FunctionalInterface
public interface ReturnObjectMapper extends Serializable{
	ObjectMapper apply(ObjectMapper t);

	public static ReturnObjectMapper of(){
		return new ReturnObjectMapper(){
			@Override
			public ObjectMapper apply(ObjectMapper u){
				return u;
			}
		};
	}

	default ReturnObjectMapper with(Consumer<ObjectMapper> c){
		return  t ->{
			c.accept(apply(t));
			return t;
		};
	}
	default ObjectMapper get(ObjectMapper t) {
		return apply(t);
	}

}
