package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.app.dto.SmartviewData;
import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.model.SmartviewLine;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.db.Dao;
import com.sifradigital.framework.db.Database;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

@Dao
public interface SmartviewDAO {
    @RegisterFieldMapper(Smartview.class)
    @SqlQuery("SELECT * FROM smartviews WHERE id = :id")
    Smartview get(@Bind("id") int id);

    @RegisterFieldMapper(Smartview.class)
    @SqlQuery("SELECT * FROM smartviews ORDER BY id")
    List<Smartview> getAll();

    @RegisterFieldMapper(Smartview.class)
    @SqlQuery("SELECT * FROM smartviews WHERE user_id = :userId ORDER BY id")
    List<Smartview> getByUser(@Bind("userId") int userId);

    @SqlUpdate("UPDATE smartviews SET name = :name, sort_number = :sortNumber, archived = :archived, client_ids = :clientIds, updated = NOW() WHERE id = :id")
    void updateSmartview(@BindBean Smartview smartview);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO smartviews (user_name, user_id, name, sort_number, archived) VALUES (:userName, :userId, :name, :sortNumber, :archived)")
    int createSmartview(@BindBean Smartview smartview);

    @RegisterFieldMapper(SmartviewLine.class)
    @SqlQuery("SELECT * FROM smartview_lines WHERE smartview_id = :smartviewId ORDER BY id")
    List<SmartviewLine> getSmartviewLines(@Bind("smartviewId") int smartviewId);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO smartview_lines (smartview_id, query, class_to_join, field_to_search, search_value, operator, type) VALUES (:smartviewId, :query, :classToJoin, :fieldToSearch, :searchValue, :operator, :type)")
    int createSmartviewLine(@BindBean SmartviewLine smartviewLine);

    @SqlUpdate("UPDATE smartview_lines SET query = :query, class_to_join = :classToJoin, field_to_search = :fieldToSearch, search_value = :searchValue, operator = :operator, type = :type, updated = now() WHERE id = :id")
    void updateSmartviewLine(@BindBean SmartviewLine smartviewLine);

    default Smartview create(User user, SmartviewData smartviewData) {
        Smartview smartview = new Smartview(
                null,
                user.getUsername(),
                user.getId(),
                smartviewData.getName(),
                smartviewData.getSortNumber(),
                smartviewData.isArchived(),
                null,
                null
        );
        int id = createSmartview(smartview);
        if (smartviewData.getSmartviewLines() != null) {
            for (SmartviewLine smartviewLine : smartviewData.getSmartviewLines()) {
                smartviewLine.setSmartviewId(id);
                createSmartviewLine(smartviewLine);
            }
        }
        return get(id);
    }

    default Smartview update(User user, int smartviewId, SmartviewData smartviewData) {
        if (smartviewData.getId() != smartviewId || smartviewData.getUserId() != user.getId()) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Smartview smartview = new Smartview(
                smartviewData.getId(),
                smartviewData.getUserName(),
                smartviewData.getUserId(),
                smartviewData.getName(),
                smartviewData.getSortNumber(),
                smartviewData.isArchived(),
                null,
                new Date()
        );
        updateSmartview(smartview);

        if (smartviewData.getSmartviewLines() != null) {
            for (SmartviewLine smartviewLine : smartviewData.getSmartviewLines()) {
                if (smartviewLine.getId() == null) {
                    smartviewLine.setSmartviewId(smartviewId);
                    createSmartviewLine(smartviewLine);
                }
                else if (smartviewLine.getSmartviewId() == smartviewId) {
                    updateSmartviewLine(smartviewLine);
                }
            }
        }

        return Database.dao(SmartviewDAO.class).get(smartviewId);

    }
}
