package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.dao.mapper.SmartviewReducer;
import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.model.SmartviewLine;
import com.sifradigital.framework.db.Dao;
import com.sifradigital.framework.db.Database;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.*;

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
    @SqlQuery("SELECT s.id as s_id, s.user_name s_user_name, s.user_id as s_user_id, s.name as s_name, s.sort_number as s_sort_number, s.archived as s_archived, s.client_ids as s_client_ids, s.created as s_created, s.updated as s_updated, sl.* FROM smartviews s LEFT JOIN smartview_lines sl ON s.id = sl.smartview_id ORDER BY id")
    List<Smartview> getAll();

    @RegisterFieldMapper(value = Smartview.class, prefix = "s")
    @RegisterFieldMapper(SmartviewLine.class)
    @UseRowReducer(SmartviewReducer.class)
    @SqlQuery("SELECT s.id as s_id, s.user_name s_user_name, s.user_id as s_user_id, s.name as s_name, s.sort_number as s_sort_number, s.archived as s_archived, s.client_ids as s_client_ids, s.created as s_created, s.updated as s_updated, sl.* FROM smartviews s JOIN smartview_lines sl ON s.id = sl.smartview_id WHERE user_id = :userId ORDER BY id")
    List<Smartview> getByUser(@Bind("userId") int userId);

    @SqlUpdate("UPDATE smartviews SET name = :name, sort_number = :sortNumber, archived = :archived, client_ids = :clientIds, user_id = :userId, updated = NOW() WHERE id = :id")
    void updateSmartview(@BindBean Smartview smartview);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO smartviews (user_name, user_id, name, sort_number, archived) VALUES (:userName, :userId, :name, :sortNumber, :archived)")
    int createSmartview(@BindBean Smartview smartview);

    @RegisterFieldMapper(SmartviewLine.class)
    @SqlQuery("SELECT * FROM smartview_lines WHERE id = :smartviewLineId")
    SmartviewLine getSmartviewLine(@Bind("smartviewLineId") int smartviewLineId);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO smartview_lines (smartview_id, group_num, table_name, field, search_value, operator, type) VALUES (:smartviewId, :groupNum, :tableName, :field, :searchValue, :operator, :type)")
    int createSmartviewLine(@BindBean SmartviewLine smartviewLine);

    @SqlUpdate("DELETE FROM smartview_lines WHERE smartview_id = :smartviewId")
    void deleteSmartviewLines(@Bind("smartviewId") int smartviewId);

    @SqlUpdate("DELETE FROM smartviews WHERE id = :id")
    void delete(@Bind("id") int id);

    default Smartview create(Smartview smartview) {
        int id = createSmartview(smartview);
        for (SmartviewLine smartviewLine : smartview.getSmartviewLines()) {
            smartviewLine.setSmartviewId(id);
            createSmartviewLine(smartviewLine);
        }
        return get(id);
    }

    default Smartview update(Smartview smartview) {
        updateSmartview(smartview);

        deleteSmartviewLines(smartview.getId());

        for (SmartviewLine smartviewLine : smartview.getSmartviewLines()) {
            smartviewLine.setSmartviewId(smartview.getId());
            createSmartviewLine(smartviewLine);
        }

        return Database.dao(SmartviewDAO.class).get(smartview.getId());
    }
}
