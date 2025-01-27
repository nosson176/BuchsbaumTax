package com.buchsbaumtax.app.domain.client;

import com.buchsbaumtax.core.dao.ClientDAO;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Client;
import com.buchsbaumtax.core.model.CustomerContactInfo;
import com.buchsbaumtax.core.model.Smartview;
import com.buchsbaumtax.core.util.NaturalOrderComparator;
import com.sifradigital.framework.db.Database;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetClients {
    static final Logger logger = LoggerFactory.getLogger(GetClients.class);
    public List<Client> getAll(Boolean active) {
        List<Client> clients;
        if (active != null) {
            clients = Database.dao(ClientDAO.class).getAll(active); // Pass active flag to DAO
        } else {
            clients = Database.dao(ClientDAO.class).getAll(false); // Default to inactive clients if active is null
        }
        sort(clients);
        return clients;
    }

    public List<Client> getForSmartview(int smartviewId, Boolean active) {
        Smartview smartview = Database.dao(SmartviewDAO.class).get(smartviewId);
        List<Client> clients = Database.dao(ClientDAO.class).getBulk(smartview.getClientIds(), active);
//        sort(clients);
        return clients;
    }

    // Get clients based on a default search query, filter by active status
    public List<Client> getForDefaultSearch(String q, Boolean active) {
        List<Client> clients = Database.dao(ClientDAO.class).getFiltered(q, active); // Pass active flag to DAO
        sort(clients);
        return clients;
    }

    // Get clients based on a specific field search, filter by active status
    public List<Client> getForFieldSearch(String q, String field, Boolean active) {
        String[] fieldArray = field.split("::");
        if (fieldArray.length < 2) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        String table = fieldArray[0];
        String fieldName = fieldArray[1];
        StringBuilder query = new StringBuilder();

        // Base query with join and filter based on table and fieldName
        query.append("SELECT DISTINCT c.*, cf.* FROM clients c LEFT JOIN client_flags cf ON c.id = cf.client_id ");
        if (!table.equals("clients")) {
            query.append(String.format("JOIN %s t ON c.id = t.client_id WHERE t.%s ILIKE '%%%s%%'", table, fieldName, q));
        } else {
            query.append(String.format("WHERE %s ILIKE '%%%s%%'", fieldName, q));
        }

        // Modify active condition logic
        if (active != null) {
            if (active) {
                query.append(" AND c.active = true");
            }
        }

        query.append(" ORDER BY c.last_name");

        // Convert query to string
        String queryString = query.toString();
        List<Client> clients = Database.dao(ClientDAO.class).getFilteredWithFields(queryString);
        return clients;
    }



    private void sort(List<Client> clients) {
        clients.sort(Comparator.comparing(Client::getLastName, new NaturalOrderComparator()));
    }

    public List<CustomerContactInfo> getExportClients(List<Client> clients) throws SQLException {
        List<CustomerContactInfo> customerContactInfos = new ArrayList<>();
//        logger.info("get parameter clients: {}", clients);

        for (Client client : clients) {
            CustomerContactInfo contactInfo = Database.dao(ClientDAO.class).getContactInfoForClient(client.getId());

            if (contactInfo != null) {
                String mainDetail = contactInfo.getMainDetail();
                // Check if mainDetail is a valid email
                String email = isValidEmail(mainDetail) ? mainDetail : "";

                customerContactInfos.add(new CustomerContactInfo(
                        client.getId(),
                        client.getLastName(),
                        contactInfo.getContactType(),
                        contactInfo.getMemo(),
                        email // Use the email or empty string
                ));
            }
        }
//        logger.info("Retrieved customerContactInfos: {}", customerContactInfos);
        return customerContactInfos;
    }

    private boolean isValidEmail(String email) {
        // Simple regex to check if the string is an email
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email != null && email.matches(emailRegex);
    }

}
