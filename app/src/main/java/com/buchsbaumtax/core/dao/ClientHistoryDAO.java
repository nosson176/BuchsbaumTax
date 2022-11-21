package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.core.model.Client;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Dao
public interface ClientHistoryDAO {

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO client_history (user_id, client_id) VALUES (:userId, :clientId)")
    int create(@Bind("userId") int userId, @Bind("clientId") int clientId);

    @RegisterFieldMapper(Client.class)
    @SqlQuery("SELECT * FROM (SELECT DISTINCT ON(c.id) c.*, ch.created as ch_created FROM client_history ch JOIN clients c on c.id = ch.client_id WHERE user_id = :userId) AS cc ORDER BY ch_created DESC LIMIT :limit")
    List<Client> getRecentByUser(@Bind("userId") int userId, @Bind("limit") int limit);
}
