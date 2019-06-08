import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import classinfo.*;

public class Parsing {
	private enum ParsingStatus {
		NORMAL,					// 최초 상태(쓰일일 없음)
		IN_COMMENT, 			// /* */ 와 같은 주석 상태일 때
		SKIP_LINE,				// //로 인한 한 줄 주석
		DECLARE_CLASS,			// 클래스 선언중인 상태
		DECLARE_METHOD,			// 메소드 정의중인 상태
		DECLARE_VARIABLE,
		IN_CLASS_BLOCK,			// 클래스 정의 블록
		IN_METHOD_BLOCK,		// 메소드 정의 블록
		SET_CLASS_NAME,			// 클래스 이름 지정
		SET_PROPERTY_NAME,		// 변수, 메소드의 이름 지정
		IN_PARAMETER,
	}
	
	private ClassInformation classInfo;
	private String accessType;
	private Stack<ParsingStatus> statuses;
	private Stack<PropertyData> props;
	private List<ClassVariable> params;
	
	private Map<Pattern, Consumer<String>> patterns;
	
	public Parsing() {
		classInfo = new ClassInformation();
		accessType = "public";
		statuses = new Stack<ParsingStatus>();
		statuses.push(ParsingStatus.NORMAL);
		props = new Stack<PropertyData>();
		params = new ArrayList<ClassVariable>();
		initializePatterns();
	}
	
	public void initializePatterns() {
		patterns = new LinkedHashMap<Pattern, Consumer<String>>();
		patterns.put(Pattern.compile("class"), this::setClassName);
		patterns.put(Pattern.compile("(public|protected|private):"), this::setAccessType);
		patterns.put(Pattern.compile("[{]"), this::startBlock);
		patterns.put(Pattern.compile("//"), this::skipByLineComment);
		patterns.put(Pattern.compile("[(]"), this::startParameterBlock);
		patterns.put(Pattern.compile(","), this::nextParameter);
		patterns.put(Pattern.compile("(void|int|char|short|long|float|double|bool)"), this::addProperty);
		patterns.put(Pattern.compile("[)]"), this::endParameterBlock);
		patterns.put(Pattern.compile("[}]"), this::endBlock);
		patterns.put(Pattern.compile(";"), this::endLine);
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
			String word = st.nextToken();
			// Property의 이름을 받음
			switch(statuses.lastElement()) {
			case SET_CLASS_NAME:
				String name = word.split("[{| ]")[0];
				classInfo.setName(name);
				statuses.pop();
				statuses.push(ParsingStatus.DECLARE_CLASS);
				patterns = addClassMethodNamePattern(name);
				break;
			case SET_PROPERTY_NAME:
				props.lastElement().setName(word.split("[(| |)|;]")[0]);
				statuses.pop();
				// 남은 라인에서
				if(line.indexOf('(') == -1 || statuses.search(ParsingStatus.IN_PARAMETER) != -1) {
					statuses.push(ParsingStatus.DECLARE_VARIABLE);
				} else {
					statuses.push(ParsingStatus.DECLARE_METHOD);
				}
				break;
			}
			
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
	
	private LinkedHashMap<Pattern, Consumer<String>> addClassMethodNamePattern(String name) {
		LinkedHashMap<Pattern, Consumer<String>> tempMap = new LinkedHashMap<Pattern, Consumer<String>>();
		tempMap.put(Pattern.compile("(" + name + "|~" + name + ")"), this::addClassMethod);
		tempMap.putAll(patterns);
		return tempMap;
	}
	
	private void setClassName(String word) {
		statuses.push(ParsingStatus.SET_CLASS_NAME);
	}
	
	private void setAccessType(String word) {
		accessType = word.split(":")[0];
	}
	
	private void startBlock(String word) {
		if(statuses.search(ParsingStatus.DECLARE_METHOD) != -1) {
			statuses.push(ParsingStatus.IN_METHOD_BLOCK);
			System.out.println("IN METHOD");
		} else {
			statuses.push(ParsingStatus.IN_CLASS_BLOCK);
			System.out.println("IN CLASS");
		}
	}
	
	private void endBlock(String word) {
		if(statuses.search(ParsingStatus.DECLARE_METHOD) != -1) {
			ClassMethod method = new ClassMethod(props.pop());
			method.getParameters().addAll(params);
			params.clear();
			classInfo.getMethods().add(method);
			statuses.remove(statuses.lastIndexOf(ParsingStatus.IN_METHOD_BLOCK));
			statuses.remove(statuses.lastIndexOf(ParsingStatus.DECLARE_METHOD));
			System.out.println("OUT METHOD");
		} else {
			statuses.remove(statuses.lastIndexOf(ParsingStatus.IN_CLASS_BLOCK));
			statuses.remove(statuses.lastIndexOf(ParsingStatus.DECLARE_CLASS));
			System.out.println("OUT CLASS");
		}
	}

	private void skipByLineComment(String word) {
		statuses.push(ParsingStatus.SKIP_LINE);
	}
	
	private void addClassMethod(String word) {
		if(statuses.search(ParsingStatus.IN_CLASS_BLOCK) != -1) {
			PropertyData tempProperty = new PropertyData();
			tempProperty.setType("void");
			tempProperty.setName(getPatternWord(
					"(" + classInfo.getName() + "|~" + classInfo.getName() + ")",
					word
			));
			props.push(tempProperty);
			statuses.push(ParsingStatus.DECLARE_METHOD);
		}
	}
	
	private void addProperty(String word) {
		if(statuses.search(ParsingStatus.DECLARE_CLASS) != -1) {
			PropertyData tempProperty; 
			tempProperty = new PropertyData();
			if(statuses.search(ParsingStatus.IN_PARAMETER) == -1) {
				tempProperty.setAccess(accessType);
			}
			tempProperty.setType(getPatternWord(
					"(void|int|char|short|long|float|double|bool)",
					word
			));
			props.push(tempProperty);
			statuses.push(ParsingStatus.SET_PROPERTY_NAME);
		}
	}
	
	private void startParameterBlock(String word) {
		statuses.push(ParsingStatus.IN_PARAMETER);
	}
	
	private void endParameterBlock(String word) {
		statuses.remove(statuses.lastIndexOf(ParsingStatus.IN_PARAMETER));
		if(statuses.search(ParsingStatus.DECLARE_VARIABLE) != -1) {
			statuses.remove(statuses.lastIndexOf(ParsingStatus.DECLARE_VARIABLE));
			params.add(new ClassVariable(props.pop()));
		}
	}
	
	private void endLine(String word) {
		if(statuses.search(ParsingStatus.IN_METHOD_BLOCK) == -1) {
			if(statuses.search(ParsingStatus.IN_PARAMETER) == -1) {
				if(statuses.search(ParsingStatus.DECLARE_METHOD) != -1) {
					statuses.remove(statuses.lastIndexOf(ParsingStatus.DECLARE_METHOD));
					ClassMethod method = new ClassMethod(props.pop());
					method.getParameters().addAll(params);
					params.clear();
					classInfo.getMethods().add(method);
				} else if(statuses.search(ParsingStatus.DECLARE_VARIABLE) != -1) {
					statuses.remove(statuses.lastIndexOf(ParsingStatus.DECLARE_VARIABLE));
					classInfo.getVariables().add(new ClassVariable(props.pop()));
				}
			}
		}
	}
	
	private void nextParameter(String word) {
		if(statuses.search(ParsingStatus.IN_PARAMETER) == -1) {
			statuses.remove(statuses.lastIndexOf(ParsingStatus.DECLARE_VARIABLE));
		}
	}
	
	private static String getPatternWord(String regex, String word) {
		Matcher m = Pattern.compile(regex).matcher(word);
		while(m.find()) {
			return m.group(1);
		}
		return "";
	}
}
