package com.orion.regression.parser;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.orion.regression.bean.Outcome;
import com.orion.regression.bean.ParseOutcome;
import com.orion.regression.bean.Redundancy;
import com.orion.regression.bean.Tuple;

public class JSONParser {
	private static final Logger log = Logger.getLogger(JSONParser.class.getName());
	
	static final int BUFFER_SIZE = 4096;
	Class<?> tupleClass;
	ParseOutcome parseOutcome;

	public JSONParser(String tupleClassStr) {
		try {
			this.tupleClass = Class.forName(tupleClassStr);			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	int tupleIndex = 0;
	Map<String, Tuple> tupleMap = null;

	Map<String, Tuple> getTupleMap() {
		return this.tupleMap;
	}

	public ParseOutcome parse(String filePathStr) {
		File file = new File(filePathStr);
		StringBuffer stringBuffer = new StringBuffer();
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = 0;
			while ((bytesRead = bis.read(buffer)) != -1) {
				stringBuffer.append(new String(buffer, 0, bytesRead, "UTF-8"));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		parseJsonStr(stringBuffer.toString());
		return parseOutcome;
	}

	public ParseOutcome parseJsonStr(String jsonStr) {
		parseOutcome = new ParseOutcome();
		tupleIndex = 0;
		tupleMap = new HashMap<String, Tuple>();
		// Patch Fix for Mapping and Transformation anomaly - begin
		//jsonStr = jsonStr.replaceAll("\"(M|T)([0-9][0-9])", "\"($1)99");
		jsonStr = jsonStr.replaceAll("\"(M|T)([0-9]+)", "\"$19");
		// jsonStr.replaceAll(".sql.([0-9])+\"", ".sql.99\"");
		// Patch Fix for Mapping and Transformation anomaly - end
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Map<String, Object> map = (Map<String, Object>) objectMapper.readValue(jsonStr, Map.class);
			Object innerObject = map.get("result");
			if (innerObject != null) {
				if (innerObject instanceof List<?>) {
					recurseList(innerObject);
				}
			}
		} catch (JsonMappingException jme) {
		} catch (JsonParseException jpe) {
		} catch (IOException ioe) {
		}
		parseOutcome.setParseMap(tupleMap);
		return parseOutcome;
	}

	private void recurseList(final Object object) throws UnsupportedEncodingException {
		for (Object innerObject : (List<Object>) object) {
			if (innerObject instanceof Map<?, ?>) {
				iterateResultMap((Map<String, String>) innerObject);
			}
		}
	}

	private void iterateResultMap(final Map<String, String> map) throws UnsupportedEncodingException {
		Constructor<?> constructor = null;
		try {
			constructor = tupleClass.getConstructor(Integer.class);
		} catch (NoSuchMethodException | SecurityException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		Tuple tuple = null;
		try {
			tuple = (Tuple) constructor.newInstance(++tupleIndex);
		} catch (InstantiationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IllegalArgumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (InvocationTargetException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		for (String key : map.keySet()) {
			Object valueObj = map.get(key);
			String valueStr = null;
			if ("parent".equals(key)) {
				valueStr = getAbsolutePath(valueObj);
			} else if ("source".equals(key)) {
				valueStr = getAbsolutePath(valueObj);
			} else if ("target".equals(key)) {
				valueStr = getAbsolutePath(valueObj);
			} else {
				valueStr = (valueObj instanceof Integer) ? valueObj.toString() : (String) valueObj;
			}
			PropertyDescriptor propertyDescriptor = null;
			try {
				propertyDescriptor = new PropertyDescriptor(key, this.tupleClass);
			} catch (IntrospectionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Method setter = propertyDescriptor.getWriteMethod();
			try {
				setter.invoke(tuple, valueStr != null ? valueStr.trim() : null);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		tuple.setUniqueName();
		if (tupleMap.get(tuple.getDigest_id()) != null) {
			Tuple existingTuple = tupleMap.get(tuple.getDigest_id());
			// redundant HashMap keys are present.
			log.log(Level.DEBUG, "Something fishy!! Line#" + tuple.getIndex() + "&Id#"+tuple.getUnique_id()+" already exists on Line#"
					+ existingTuple.getIndex() + "&Id#"+existingTuple.getUnique_id() + " -> " + tuple.getUniqueName());
			Redundancy redundancy = new Redundancy(tuple, existingTuple);
			parseOutcome.addRedundancy(redundancy);
		}
		tupleMap.put(tuple.getDigest_id(), tuple);
	}

	private String getAbsolutePath(Object obj) throws UnsupportedEncodingException {
		Deque<String> stack = new ArrayDeque<String>();
		if (obj != null) {
			if (obj instanceof List<?>) {
				recursePathFromList(obj, stack);
			} else if (obj instanceof Map<?, ?>) {
				recursePathFromMap((Map<String, Object>) obj, stack);
			}
		}
		return stack.stream().collect(Collectors.joining("#"));
	}

	private void recursePathFromList(final Object object, Deque<String> stack) throws UnsupportedEncodingException {
		for (Object innerObject : (List<Object>) object) {
			if (innerObject instanceof Map<?, ?>) {
				recursePathFromMap((Map<String, Object>) innerObject, stack);
			}
		}
	}

	private void recursePathFromMap(final Map<String, Object> map, Deque<String> stack)
			throws UnsupportedEncodingException {
		for (String key : map.keySet()) {
			Object innerObject = map.get(key);
			if (innerObject != null) {
				if (innerObject instanceof List<?>) {
					recursePathFromList(innerObject, stack);
				} else if (innerObject instanceof Map<?, ?>) {
					recursePathFromMap((Map<String, Object>) innerObject, stack);
				} else if ("name".equals(key) || "nm".equals(key)) {
					String value = (String) innerObject;
					if (value != null && value.trim().length() > 0) {
						stack.push((String) innerObject);
					}
				}
			}
		}
	}
}
