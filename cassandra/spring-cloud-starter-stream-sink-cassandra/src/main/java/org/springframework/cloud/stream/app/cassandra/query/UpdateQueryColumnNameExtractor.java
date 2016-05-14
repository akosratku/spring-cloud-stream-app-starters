package org.springframework.cloud.stream.app.cassandra.query;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

/**
 * 
 * @author Akos Ratku
 *
 */
public class UpdateQueryColumnNameExtractor implements ColumnNameExtractor {

	private static final Pattern PATTERN = Pattern.compile("(?<=set)(.*)(?=where)where(.*)");
	
	@Override
	public List<String> extract(String query) {
		List<String> extractedColumns = new LinkedList<>();
		Matcher matcher = PATTERN.matcher(query);
		if (matcher.find()) {
			String[] settings = StringUtils.delimitedListToStringArray(matcher.group(1), ",", " ");
			String[] where = StringUtils.delimitedListToStringArray(matcher.group(2), ",", " ");
			readPairs(extractedColumns, settings);
			readPairs(extractedColumns, where);
		}
		else {
			throw new IllegalArgumentException("Invalid CQL update query syntax: " + query);
		}
		return extractedColumns;
	}

	protected void readPairs(List<String> extractedcColumns, String[] settings) {
		for (String setting : settings) {
			String[] columnValuePair = StringUtils.delimitedListToStringArray(setting, "=", " ");
			if(columnValuePair[1].startsWith(":") || columnValuePair[1].equals("?")) {
				extractedcColumns.add(columnValuePair[0]);
			}
		}
	}

}
