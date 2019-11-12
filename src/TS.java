import java.lang.reflect.Field;
import java.util.HashMap;

public class TS {

	HashMap<String, Token> ts;

	public TS() {
		ts = new HashMap();
	}
	
	//https://stackoverflow.com/a/2043485/2396999
	public void setAtributo(String identificador, String atributo, String valor) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		Token t;
		t = ts.get(identificador);
		Field f = t.getClass().getDeclaredField(atributo);
		f.set(t, valor);
		
	}
	
	//https://stackoverflow.com/a/2043485/2396999
	public String getAtributo(String identificador, String atributo) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Token t;
		t = ts.get(identificador);
		Field f = t.getClass().getDeclaredField(atributo);
		return (String) f.get(t);
	}
	
}
