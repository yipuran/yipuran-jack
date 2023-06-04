package org.yipuran.json.databind;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.NamingBase;
/**
 * 大文字の Snake Case の data bind Strategy.
 * <pre>
 * （使用例）
 * ObjectMapper mapper = new ObjectMapper();
 * mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
 * mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
 *
 * mapper.setPropertyNamingStrategy(new UpperSnakeCaseStrategy());
 *
 * </pre>
 * @since 1.2
 */
public class UpperSnakeCaseStrategy extends NamingBase{
    @Override
    public String translate(String input){
        if (input == null) return input;
        int length = input.length();
   	  StringBuilder result = new StringBuilder(length * 2);
        int resultLength = 0;
        boolean wasPrevTranslated = false;
        for(int i = 0; i < length; i++){
            char c = input.charAt(i);
            if (i > 0 || c != '_'){
                if (Character.isUpperCase(c)){
                    if (!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != '_'){
                        result.append('_');
                        resultLength++;
                    }
                    c = Character.toLowerCase(c);
                    wasPrevTranslated = true;
                }else{
                    wasPrevTranslated = false;
                }
                result.append(c);
                resultLength++;
            }
        }
        return resultLength > 0 ? result.toString().toUpperCase() : input.toUpperCase();
    }
}
