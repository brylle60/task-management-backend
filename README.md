# Task Management System

This project has been split into separate repositories:

- **Backend**: [task-management-backend](https://github.com/brylle60/task-management-backend)
- **Frontend**: [task-management-frontend](https://github.com/brylle60/task-management-frontend.git)

Please refer to individual repositories for setup instructions.

```

---

## 📂 **Final Structure:**



brylle60/
├── task-management-backend/     (Backend repo)
│   ├── src/
│   ├── pom.xml
│   ├── .env (not committed)
│   ├── .env.example
│   └── README.md
│
└── task-management-frontend/    (Frontend repo)
    ├── src/
    ├── package.json
    ├── .env (not committed)
    ├── .env.example
    └── README.md

```

# Task Management Automation

This project consists of a Spring Boot backend and a React frontend with TypeScript and Tailwind CSS.

## Getting Started

### Backend (Spring Boot)

To run the backend, navigate to the `backend` directory and use Maven:

```bash
cd backend
./mvnw spring-boot:run
```

The backend will be accessible at `http://localhost:8080`.

### Frontend (React + TypeScript + Tailwind CSS)

To run the frontend, navigate to the `frontend` directory and use npm:

```bash
cd frontend
npm install
npm run dev
```

The frontend will be accessible at `http://localhost:5173` (or another port if 5173 is in use).
