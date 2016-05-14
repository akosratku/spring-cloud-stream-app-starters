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
public class InsertQueryColumnNameExtractor implements ColumnNameExtractor {

	private static final Pattern PATTERN = Pattern.compile(".+\\((.+)\\).+(?:values\\s*\\((.+)\\))");
	
	@Override
	public List<String> extract(String query) {
		List<String> extractedColumns = new LinkedList<>();
		Matcher matcher = PATTERN.matcher(query);
		if (matcher.matches()) {
			String[] columns = StringUtils.delimitedListToStringArray(matcher.group(1), ",", " ");
			String[] params = StringUtils.delimitedListToStringArray(matcher.group(2), ",", " ");
			for (int i = 0; i < columns.length; i++) {
				String param = params[i];
				if (param.equals("?")) {
					extractedColumns.add(columns[i]);
				}
				else if (param.startsWith(":")) {
					extractedColumns.add(param.substring(1));
				}
			}
		}
		else {
			throw new IllegalArgumentException("Invalid CQL insert query syntax: " + query);
		}
		return extractedColumns;
	}

}
