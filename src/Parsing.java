import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import classinfo.*;

public class Parsing {
	private enum ParsingStatus {
		NORMAL,					// 최초 상태(쓰일일 없음)
		IN_COMMENT, 			// /* */ 와 같은 주석 상태일 때
		SKIP_LINE,				// //로 인한 한 줄 주석
		DECLARE_CLASS,			// 클래스 선언중인 상태
		DECLARE_METHOD,			// 메소드 정의중인 상태
		SET_CLASS_NAME,			// 클래스 이름 지정
		SET_PROPERTY_NAME,		// 변수, 메소드의 이름 지정
	}
	
	private ClassInformation classInfo;
	private String accessType;
	private Stack<ParsingStatus> statuses;
	
	private Map<Pattern, Consumer<String>> patterns;
	
	public Parsing() {
		classInfo = new ClassInformation();
		accessType = "public";
		statuses = new Stack<ParsingStatus>();
		statuses.push(ParsingStatus.NORMAL);
		initializePatterns();
	}
	
	public void initializePatterns() {
		patterns = new HashMap<Pattern, Consumer<String>>();
		patterns.put(Pattern.compile("class"), this::setClassName);
		patterns.put(Pattern.compile("(public|protected|private):"), this::setAccessType);
		patterns.put(Pattern.compile("[{]"), this::startBlock);
		patterns.put(Pattern.compile("//"), this::skipByLineComment);
		patterns.put(Pattern.compile("(void|int|char|short|long|float|double|bool)"), this::addProperty);
		patterns.put(Pattern.compile("[}]"), this::endBlock);
	}
	
	public ClassInformation parse(StringBuffer buffer) {
		StringTokenizer st = new StringTokenizer(buffer.toString(), "\n", false);
		while(st.hasMoreTokens()) {
			String line = st.nextToken();
			processByLine(line);
		}
		
		return classInfo;
	}
	
	private void processByLine(String line) {
		StringTokenizer st = new StringTokenizer(line, " ", false);
		while(st.hasMoreTokens()) {
			switch(statuses.lastElement()) {
			case SET_CLASS_NAME:
				break;
			case SET_PROPERTY_NAME:
				break;
			}
			
			String word = st.nextToken();
			patterns.keySet().stream()
				.filter(p -> p.asPredicate().test(word))
				.map(patterns::get)
				.forEach(c -> c.accept(word));
			
			if(statuses.lastElement() == ParsingStatus.SKIP_LINE) {
				statuses.pop();
				break;
			}
		}
	}
	
	private void setClassName(String word) {
		statuses.push(ParsingStatus.DECLARE_CLASS);
		statuses.push(ParsingStatus.SET_CLASS_NAME);
	}
	
	private void setAccessType(String word) {
		accessType = word.split(":")[0];
	}
	
	private void startBlock(String word) {
		if(statuses.search(ParsingStatus.DECLARE_CLASS) == -1
				&& statuses.search(ParsingStatus.DECLARE_METHOD) == -1) {
			statuses.push(ParsingStatus.DECLARE_CLASS);
			System.out.println("IN CLASS");
		} else {
			statuses.push(ParsingStatus.DECLARE_METHOD);
			System.out.println("IN METHOD");
		}
	}
	
	private void endBlock(String word) {
		int index;
		if(statuses.search(ParsingStatus.DECLARE_METHOD) != -1) {
			index = statuses.lastIndexOf(ParsingStatus.DECLARE_METHOD);
			System.out.println("OUT METHOD");
		} else {
			index = statuses.lastIndexOf(ParsingStatus.DECLARE_CLASS);
			System.out.println("OUT CLASS");
		}
		statuses.remove(index);
	}

	private void skipByLineComment(String word) {
		statuses.push(ParsingStatus.SKIP_LINE);
	}
	
	private void addProperty(String word) {
		PropertyData data;
		
		
	}
}
