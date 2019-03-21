package ch.so.agi.apachecamel.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import ch.so.agi.apachecamel.models.ExecutionMessage;

@Service
public class ExecutionMessageDAOImpl implements ExecutionMessageDAO {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ExecutionMessage> getExecutionMessages() {
        String sql = ""
                + "SELECT\n" + 
                "  aimport.importdate,\n" + 
                "  vollzugsgegenstand.t_datasetname AS datasetname,\n" + 
                "  vollzugsgegenstand.status AS status,\n" + 
                "  TO_DATE(vollzugsgegenstand.grundbucheintrag, 'YYYY-MM-DD') AS landRegisterEntryDate,\n" + 
                "  TO_DATE(vollzugsgegenstand.tagebucheintrag, 'YYYY-MM-DD') AS journalEntryDate,\n" + 
                "  mutationsnummer.nummer AS referenceNumber,\n" + 
                "  mutationsnummer.nbident AS identnd\n" + 
                "FROM\n" + 
                "  agi_gb2av.vollzugsgegnstnde_vollzugsgegenstand AS vollzugsgegenstand\n" + 
                "  LEFT JOIN agi_gb2av.mutationsnummer AS mutationsnummer\n" + 
                "  ON mutationsnummer.vollzgsggnszgsggnstand_mutationsnummer = vollzugsgegenstand.t_id\n" + 
                "  LEFT JOIN agi_gb2av.t_ili2db_dataset AS dataset\n" + 
                "  ON dataset.datasetname = vollzugsgegenstand.t_datasetname\n" + 
                "  LEFT JOIN agi_gb2av.t_ili2db_import AS aimport\n" + 
                "  ON aimport.dataset = dataset.t_id\n" + 
                "ORDER BY\n" + 
                "  TO_DATE(vollzugsgegenstand.tagebucheintrag, 'YYYY-MM-DD') DESC,\n" + 
                "  TO_DATE(vollzugsgegenstand.grundbucheintrag, 'YYYY-MM-DD') DESC,\n" + 
                "  aimport.importdate DESC\n" + 
                ";";
        
        RowMapper<ExecutionMessage> rowMapper = new BeanPropertyRowMapper<ExecutionMessage>(ExecutionMessage.class);
        List<ExecutionMessage> list = jdbcTemplate.query(sql, rowMapper);
        return list;
    }
}
