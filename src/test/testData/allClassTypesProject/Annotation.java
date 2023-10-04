import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface JsonSerializable {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Init {}

@JsonSerializable
public class Person {
    @Init
    private void initNames() {}
}