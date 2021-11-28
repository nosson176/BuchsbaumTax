package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.dao.mapper.SmartviewReducer;
import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.model.SmartviewLine;
import com.sifradigital.framework.db.Dao;
import com.sifradigital.framework.db.Database;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;

import java.util.List;

@Dao
public interface SmartviewDAO {
    @RegisterFieldMapper(value = Smartview.class, prefix = "s")
    @RegisterFieldMapper(SmartviewLine.class)
    @UseRowReducer(SmartviewReducer.class)
    @SqlQuery("SELECT s.id as s_id, s.user_name s_user_name, s.user_id as s_user_id, s.name as s_name, s.sort_number as s_sort_number, s.archived as s_archived, s.client_ids as s_client_ids, s.created as s_created, s.updated as s_updated, sl.* FROM smartviews s JOIN smartview_lines sl ON s.id = sl.smartview_id WHERE s.id= :id")
    Smartview get(@Bind("id") int id);

    @RegisterFieldMapper(value = Smartview.class, prefix = "s")
    @RegisterFieldMapper(SmartviewLine.class)
    @UseRowReducer(SmartviewReducer.class)
    @SqlQuery("SELECT s.id as s_id, s.user_name s_user_name, s.user_id as s_user_id, s.name as s_name, s.sort_number as s_sort_number, s.archived as s_archived, s.client_ids as s_client_ids, s.created as s_created, s.updated as s_updated, sl.* FROM smartviews s JOIN smartview_lines sl ON s.id = sl.smartview_id ORDER BY id")
    List<Smartview> getAll();

    @RegisterFieldMapper(value = Smartview.class, prefix = "s")
    @RegisterFieldMapper(SmartviewLine.class)
    @UseRowReducer(SmartviewReducer.class)
    @SqlQuery("SELECT s.id as s_id, s.user_name s_user_name, s.user_id as s_user_id, s.name as s_name, s.sort_number as s_sort_number, s.archived as s_archived, s.client_ids as s_client_ids, s.created as s_created, s.updated as s_updated, sl.* FROM smartviews s JOIN smartview_lines sl ON s.id = sl.smartview_id WHERE user_id = :userId ORDER BY id")
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

    default Smartview create(Smartview smartview) {
        int id = createSmartview(smartview);
        if (smartview.getSmartviewLines() != null) {
            for (SmartviewLine smartviewLine : smartview.getSmartviewLines()) {
                smartviewLine.setSmartviewId(id);
                createSmartviewLine(smartviewLine);
            }
        }
        return get(id);
    }

    default Smartview update(Smartview smartview) {
        updateSmartview(smartview);

        if (smartview.getSmartviewLines() != null) {
            for (SmartviewLine smartviewLine : smartview.getSmartviewLines()) {
                if (smartviewLine.getId() == null) {
                    smartviewLine.setSmartviewId(smartview.getId());
                    createSmartviewLine(smartviewLine);
                }
                else if (smartviewLine.getSmartviewId() == smartview.getId()) {
                    updateSmartviewLine(smartviewLine);
                }
            }
        }

        return Database.dao(SmartviewDAO.class).get(smartview.getId());
    }
}