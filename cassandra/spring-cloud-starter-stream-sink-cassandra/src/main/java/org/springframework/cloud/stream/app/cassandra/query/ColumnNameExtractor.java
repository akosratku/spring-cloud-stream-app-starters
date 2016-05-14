package org.springframework.cloud.stream.app.cassandra.query;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.integration.cassandra.outbound.CassandraMessageHandler;

/**
 * 
 * @author Akos Ratku
 *
 */
public interface ColumnNameExtractor {

	List<String> extract(String query);
	
}
