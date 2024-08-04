### Setting Up Your Local Backend

#### Steps:

1. **Install Java JDK** ☕

    - Download and install the Java Development Kit (JDK) appropriate for your system from the official Oracle or OpenJDK website.
    - Verify your installation by running `java -version` and `javac -version` in your terminal. You should see the installed Java versions.

2. **Install Apache Maven** 📦

    - Download and install Apache Maven from the official website.
    - Verify your installation by running `mvn -version` in your terminal.

3. **Install PostgreSQL** 🐘

    - Download and install PostgreSQL from the official website or using a package manager like Homebrew (for macOS).
    - During installation, set up a user named "postgres" with either no password or use "sifra123" as the password.

4. **Create a PostgreSQL Database** 🗃️

    - Open a terminal and log in to PostgreSQL with the command: `psql -U postgres`.
    - Create a database named "torahlive" by running: `CREATE DATABASE buchsbaum;`
    - Exit the PostgreSQL prompt with `\q`.

5. **Setup database** 📥

    - Execute the statements in `create.sql` to create the basic database structure. For example: `psql -U postgres -d buchsbaum < create.sql`.

6. **Build App** 🛠️

    - Navigate to the "BuchsbaumTax" project directory.
    - Run `mvn clean install` to build the .war file of the BuchsbaumTax app.

7. **Download and Install Apache Tomcat** 🌐

    - Download the Apache Tomcat server from the official website.
    - Follow the installation instructions for your specific platform.

8. **Verify Tomcat Installation** ✔️

    - Start Tomcat and ensure it's running properly by accessing it in your web browser (usually at `http://localhost:8080`).

9. **Delete ROOT Folder in Tomcat Webapps** 🚫

    - Remove the existing "ROOT" folder from the `/webapps/` directory of your Tomcat installation. This ensures that Tomcat doesn't conflict with your application.

10. **Copy .war File to Tomcat Webapps** 📦

    - Copy the generated `ROOT.war` file from the `/app/target/` directory of your BuchsbaumTax project.
    - Paste it into the `/webapps/` directory of your Tomcat installation.

11. **Add Postgres driver to Tomcat 🐘**

    - Download the PostgreSQL JDBC driver for your version of the Java JDK from [https://jdbc.postgresql.org/download/](https://jdbc.postgresql.org/download/).
    - Place the downloaded `.jar` file into the `/lib/` directory of your Tomcat installation.

12. **Configure Frontend-Backend Connection (Optional)** 🔗
    - If you have a frontend that needs to communicate with your backend, ensure that the `BACKEND_URL` in your frontend's `.env` file points to the local port where Tomcat is running (e.g., `http://localhost:8080/api`).

Congratulations! You've successfully set up your local backend. You're now ready to develop and test your application locally. 🎉

---