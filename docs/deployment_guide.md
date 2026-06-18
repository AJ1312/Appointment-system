# Deployment and Setup Guide
## MediCare – AI Medical Appointment System

---

## Prerequisites

| Tool | Version | Purpose |
|------|---------|---------|
| JDK | 21+ | Backend runtime |
| Maven | 3.9+ (bundled via mvnw) | Build tool |
| PostgreSQL | 15+ | Database |
| Git | Any | Version control |
| Browser | Chrome / Firefox / Edge (modern) | Frontend |

---

## Step 1: Clone the Repository

```bash
git clone https://github.com/AjiteshSharma/Medical_appointment_system_Hindalco.git
cd Medical_appointment_system_Hindalco
```

---

## Step 2: Database Setup

1. Start PostgreSQL service.
2. Connect using `psql`:

```bash
psql -U postgres
```

3. Run the schema and seed data:

```sql
\i Database/schema.sql
```

4. Verify tables:

```sql
\dt
-- Should show: admin, appointment, doctor, patient, queue_entry, specialization
```

5. Verify seed data:

```sql
SELECT * FROM specialization;
SELECT * FROM doctor;
SELECT * FROM admin;
```

---

## Step 3: Configure Database Connection

Open `backend/medical-appointment-system/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD_HERE
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
server.port=8080
```

Change `YOUR_PASSWORD_HERE` to your PostgreSQL password.

---

## Step 4: Run the Backend

```bash
cd backend/medical-appointment-system
./mvnw spring-boot:run
```

On Windows:
```cmd
mvnw.cmd spring-boot:run
```

You should see:
```
Tomcat started on port(s): 8080 (http)
Started MedicalAppointmentSystemApplication
```

Test the API is running:
```bash
curl http://localhost:8080/api/specializations
```

Expected: JSON array of 10 specializations.

---

## Step 5: Open the Frontend

Simply open any HTML file in your browser:

```bash
open frontend/index.html       # macOS
start frontend/index.html      # Windows
xdg-open frontend/index.html   # Linux
```

Or navigate in your file explorer to `frontend/index.html` and double-click.

**No build step required.** The frontend is pure HTML/CSS/JavaScript.

---

## Step 6: Verify the Full Flow

1. Open `frontend/index.html`
2. Click **Get Started** → register a patient account
3. Sign in as the patient
4. Book an appointment (select a doctor and date)
5. Note the token number displayed
6. Go to **Queue Status** and enter your appointment ID
7. Sign out, then sign in as Admin (`admin@medicare.com` / `admin123`)
8. View the dashboard stats and manage appointments

---

## Admin Account

The seed SQL creates one admin automatically:

| Field | Value |
|-------|-------|
| Email | admin@medicare.com |
| Password | admin123 |

To add more admins, insert directly into the `admin` table with a SHA-256 hashed password.

---

## Stopping the Server

Press `Ctrl + C` in the terminal where the Spring Boot server is running.

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| `Connection refused` on API calls | Backend not started — run Step 4 |
| `password authentication failed` | Check `application.properties` password |
| Port 8080 already in use | Kill the existing process or change `server.port` |
| Tables not found | Run `Database/schema.sql` in psql (Step 2) |
| CORS errors in browser | Ensure `CorsConfig.java` is present — it allows all origins |
| Admin login fails | Verify admin row exists and password hash is correct |

---

## Project Structure Reference

```
Medical_appointment_system_Hindalco/
├── backend/medical-appointment-system/   ← Spring Boot project
├── frontend/                             ← HTML/CSS/JS pages
├── Database/                             ← SQL schema files
├── docs/                                 ← All documentation
├── arch_diag/                            ← Architecture diagrams
└── README.md
```
