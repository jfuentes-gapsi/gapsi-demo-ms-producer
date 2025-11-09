package mx.gapsi.gapsi_demo_ms_producer.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.gapsi.commons.model.Base;
import mx.gapsi.commons.model.Label;
import mx.gapsi.commons.utils.DbQueries;
import mx.gapsi.commons.utils.InitObject;

@Repository
public class MsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final static String FIND_ALL = "SELECT LABEL_ID, KEY, VALUE, LANGUAGE, CREATED, USER_CREATOR, UPDATED, USER_MODIFIER FROM LABELS";

    public Base findAll(Base base) {
        base.setSuccessfully(false);
        List<Label> collection;
        String baseQuery = DbQueries.buildQuery(base, FIND_ALL);
        String query = baseQuery + 
                        DbQueries.buildOrderBy(base) + 
                        DbQueries.buildPagination(base);
        HashMap<String, String> search = Objects.isNull(base.getSearch()) ? null : base.getSearch().getParameters();
        collection = jdbcTemplate.query(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query);
                        return DbQueries.buildStatement(search, ps);
                    },new BeanPropertyRowMapper<>(Label.class)
                );
        List<Object> objectList = new ArrayList<>(collection);
        int totalRows = DbQueries.getTotalRows(baseQuery, search, jdbcTemplate);
        base.setCustomDto(InitObject.toCustomDto(base, totalRows));
        base.getCustomDto().setData(objectList);
        base.setSuccessfully(true);

        return base;
    }
    
}
