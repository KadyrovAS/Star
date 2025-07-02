package star.part02.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;
import star.part02.model.Rule;

/**
 *
 */

@Component
public class RuleMapper{
    public Rule getRule(ResultSet rs) throws SQLException {
        String[] arguments = new String[]{
                rs.getString("ARGUMENT01"),
                rs.getString("ARGUMENT02"),
                rs.getString("ARGUMENT03"),
                rs.getString("ARGUMENT04"),
                };
        String query = rs.getString("QUERY");
        boolean negative = rs.getBoolean("NEGATIVE");
        return new Rule(query, arguments, negative);
    }
}
