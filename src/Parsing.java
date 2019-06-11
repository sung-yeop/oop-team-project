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
	
	private ClassInformation classInfo;		// 결과 클래스
	private String accessType;				// 전역적으로 저장된 현재 접근 제어
	private Stack<ParsingStatus> statuses;	// 현재 상태를 스택으로 쌓아놓
	private Stack<PropertyData> props;		// 클래스에 함수 또는 변수로 들어 내용들
	private List<ClassVariable> params;		// 함수의 파라미터로 저장될 변수들
	private Stack<Boolean> blockLevel;		// 클래스와 함수를 제외한 중괄호 단계
	private String methodContent;			// 함수 내용
	
	private Map<Pattern, Consumer<String>> patterns;
	
	public Parsing() {
		classInfo = new ClassInformation();
		accessType = "public";
		statuses = new Stack<ParsingStatus>();
		statuses.push(ParsingStatus.NORMAL);
		props = new Stack<PropertyData>();
		params = new ArrayList<ClassVariable>();
		blockLevel = new Stack<Boolean>();
		methodContent = "";
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
		changeMethodName();
		matchVariableAndMethod();
		return classInfo;
	}
	
	private void processByLine(String line) {
		StringTokenizer st = new StringTokenizer(line, " ", false);
		while(st.hasMoreTokens()) {
			String word = st.nextToken();
			String name = "";
			// Property의 이름을 받음
			switch(statuses.lastElement()) {
			case SET_CLASS_NAME:
				name = word.split("[{| ]")[0];
				classInfo.setName(name);
				statuses.pop();
				statuses.push(ParsingStatus.DECLARE_CLASS);
				patterns = addClassMethodNamePattern(name);
				break;
			case SET_PROPERTY_NAME:
				if(statuses.search(ParsingStatus.IN_PARAMETER) == -1 && statuses.search(ParsingStatus.IN_CLASS_BLOCK) == -1){
					name = word.split("[::|(]")[2];
				} else {
					name = word.split("[(| |)|;]")[0];
				}
				name = name.replace("*", "").replace("&", "");
				props.lastElement().setName(name);
				statuses.pop();
				if(line.indexOf('(') == -1 || statuses.search(ParsingStatus.IN_PARAMETER) != -1) {
					statuses.push(ParsingStatus.DECLARE_VARIABLE);
				} else {
					statuses.push(ParsingStatus.DECLARE_METHOD);
				}
				break;
			}
			
			if(statuses.search(ParsingStatus.IN_METHOD_BLOCK) != -1) {
				methodContent += word + " ";
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
		
		if(statuses.search(ParsingStatus.IN_METHOD_BLOCK) != -1) {
			methodContent += "\n";
		}
	}
	
	private void changeMethodName() {
		for(ClassMethod method: classInfo.getMethods()) {
			String paramInfo = "(";
			ArrayList<String> types = new ArrayList<String>();
			for(ClassVariable param: method.getParameters()) {
				types.add(param.getType());
			}
			paramInfo += String.join(",", types);
			paramInfo += ")";
			method.setName(method.getName() + paramInfo);
		}
	}
	
	private void matchVariableAndMethod() {
		for(ClassMethod method: classInfo.getMethods()) {
			if(method.getContent() != "") {
				StringTokenizer st = new StringTokenizer(method.getContent(), "[ |\n|(|)|!|/[|/]|-|+|;]", false);
				while(st.hasMoreTokens()) {
					String word = st.nextToken();
					for(ClassVariable variable: classInfo.getVariables()) {
						if(word.contains(variable.getName())) {
							if(!variable.getMethods().contains(method))
								variable.getMethods().add(method);
							if(!method.getVariables().contains(variable))
								method.getVariables().add(variable);
							break;
						}
					}
				}
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
		if(statuses.search(ParsingStatus.IN_METHOD_BLOCK) != -1) {
			blockLevel.push(true);
		} else if(statuses.search(ParsingStatus.DECLARE_METHOD) != -1) {
			statuses.push(ParsingStatus.IN_METHOD_BLOCK);
		} else {
			statuses.push(ParsingStatus.IN_CLASS_BLOCK);
		}
	}
	
	private void endBlock(String word) {
		if(blockLevel.size() != 0) {
			blockLevel.pop();
		} else if(statuses.search(ParsingStatus.DECLARE_CLASS) == -1) {
			statuses.remove(statuses.lastIndexOf(ParsingStatus.IN_METHOD_BLOCK));
			statuses.remove(statuses.lastIndexOf(ParsingStatus.DECLARE_METHOD));
			for(ClassMethod method : classInfo.getMethods()) {
				if(method.getName().equals(props.lastElement().getName()) && method.getParameters().size() == params.size()) {
					int index = 0;
					for(ClassVariable variable : method.getParameters()) {
						if(!variable.getType().equals(params.get(index).getType())) {
							break;
						}
						index++;
					}
					if(index == params.size()) {
						methodContent = replaceLast(methodContent, "}", "");
						method.setContent(methodContent);
					}
				}
			}
			params.clear();
			props.pop();
			methodContent = "";
		} else if(statuses.search(ParsingStatus.DECLARE_METHOD) != -1) {
			ClassMethod method = new ClassMethod(props.pop());
			method.getParameters().addAll(params);			
			params.clear();
			methodContent = replaceLast(methodContent, "}", "");
			method.setContent(methodContent);
			methodContent = "";
			classInfo.getMethods().add(method);
			statuses.remove(statuses.lastIndexOf(ParsingStatus.IN_METHOD_BLOCK));
			statuses.remove(statuses.lastIndexOf(ParsingStatus.DECLARE_METHOD));
		} else {
			statuses.remove(statuses.lastIndexOf(ParsingStatus.IN_CLASS_BLOCK));
			statuses.remove(statuses.lastIndexOf(ParsingStatus.DECLARE_CLASS));
		}
	}

	private void skipByLineComment(String word) {
		statuses.push(ParsingStatus.SKIP_LINE);
	}
	
	private void addClassMethod(String word) {
		if(statuses.search(ParsingStatus.IN_CLASS_BLOCK) != -1) {
			PropertyData tempProperty = new PropertyData();
			tempProperty.setType("void");
			tempProperty.setAccess(accessType);
			tempProperty.setName(getPatternWord(
					"(" + classInfo.getName() + "|~" + classInfo.getName() + ")",
					word
			));
			props.push(tempProperty);
			statuses.push(ParsingStatus.DECLARE_METHOD);
		} else if(classInfo.getMethods().size() != 0 && statuses.search(ParsingStatus.DECLARE_METHOD) == -1) {
			PropertyData tempProperty = new PropertyData();
			tempProperty.setType("void");
			String name = word.split("[::|(]")[2];
			tempProperty.setName(name);
			props.push(tempProperty);
			statuses.push(ParsingStatus.DECLARE_METHOD);
		}
	}
	
	private void addProperty(String word) {
		if(statuses.search(ParsingStatus.IN_METHOD_BLOCK) == -1) {
			PropertyData tempProperty; 
			tempProperty = new PropertyData();
			if(statuses.search(ParsingStatus.IN_PARAMETER) == -1 && statuses.search(ParsingStatus.IN_CLASS_BLOCK) != -1) {
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
	
	public String replaceLast(String text, String regex, String replacement) {
		int regexIndexOf = text.lastIndexOf(regex);
        if(regexIndexOf == -1){
            return text;
        }else{
            return text.substring(0, regexIndexOf) + replacement + text.substring(regexIndexOf + regex.length());
        }
    }
}
