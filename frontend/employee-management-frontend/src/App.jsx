import { useCallback, useEffect, useState } from "react";
import "./App.css";
import {
  deleteEmployee,
  getEmployees,
} from "./api/employeeService";
import EmployeeList from "./components/EmployeeList";
import Header from "./components/Header";
import Pagination from "./components/Pagination";

function App() {
  const [employees, setEmployees] = useState([]);
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(5);
  const [totalElements, setTotalElements] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [firstPage, setFirstPage] = useState(true);
  const [lastPage, setLastPage] = useState(true);

  const [sortBy, setSortBy] = useState("id");
  const [direction, setDirection] = useState("asc");

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const loadEmployees = useCallback(async () => {
    try {
      setLoading(true);
      setError("");

      const data = await getEmployees({
        page: pageNumber,
        size: pageSize,
        sortBy,
        direction,
      });

      setEmployees(data.content || []);
      setPageNumber(data.pageNumber ?? 0);
      setPageSize(data.pageSize ?? pageSize);
      setTotalElements(data.totalElements ?? 0);
      setTotalPages(data.totalPages ?? 0);
      setFirstPage(data.first ?? true);
      setLastPage(data.last ?? true);
    } catch (requestError) {
      console.error("Failed to load employees:", requestError);

      if (requestError.code === "ECONNABORTED") {
        setError("The request timed out. Please try again.");
      } else if (!requestError.response) {
        setError(
          "Unable to connect to the backend. Make sure Spring Boot is running on port 8080."
        );
      } else {
        setError(
          requestError.response.data?.message ||
            "Unable to load employees."
        );
      }
    } finally {
      setLoading(false);
    }
  }, [pageNumber, pageSize, sortBy, direction]);

  useEffect(() => {
    loadEmployees();
  }, [loadEmployees]);

  const handlePageChange = (newPage) => {
    if (newPage >= 0 && newPage < totalPages) {
      setPageNumber(newPage);
    }
  };

  const handlePageSizeChange = (event) => {
    setPageSize(Number(event.target.value));
    setPageNumber(0);
  };

  const handleSortChange = (event) => {
    setSortBy(event.target.value);
    setPageNumber(0);
  };

  const handleDirectionChange = (event) => {
    setDirection(event.target.value);
    setPageNumber(0);
  };

  const handleDelete = async (employee) => {
    const confirmed = window.confirm(
      `Are you sure you want to delete ${employee.fullName}?`
    );

    if (!confirmed) {
      return;
    }

    try {
      setError("");
      setSuccessMessage("");

      await deleteEmployee(employee.id);

      setSuccessMessage(
        `${employee.fullName} was deleted successfully.`
      );

      if (employees.length === 1 && pageNumber > 0) {
        setPageNumber((currentPage) => currentPage - 1);
      } else {
        await loadEmployees();
      }
    } catch (requestError) {
      console.error("Failed to delete employee:", requestError);

      setError(
        requestError.response?.data?.message ||
          "Unable to delete the employee."
      );
    }
  };

  return (
    <div className="app">
      <Header />

      <main className="main-content">
        <section className="summary-section">
          <div>
            <h2>Employees</h2>
            <p>
              View and manage employees stored in your MySQL database.
            </p>
          </div>

          <div className="employee-count">
            <span>Total employees</span>
            <strong>{totalElements}</strong>
          </div>
        </section>

        <section className="controls-section">
          <div className="control-group">
            <label htmlFor="pageSize">Rows per page</label>

            <select
              id="pageSize"
              value={pageSize}
              onChange={handlePageSizeChange}
            >
              <option value={5}>5</option>
              <option value={10}>10</option>
              <option value={20}>20</option>
            </select>
          </div>

          <div className="control-group">
            <label htmlFor="sortBy">Sort by</label>

            <select
              id="sortBy"
              value={sortBy}
              onChange={handleSortChange}
            >
              <option value="id">ID</option>
              <option value="firstName">First name</option>
              <option value="lastName">Last name</option>
              <option value="department">Department</option>
              <option value="jobTitle">Job title</option>
              <option value="salary">Salary</option>
              <option value="hireDate">Hire date</option>
            </select>
          </div>

          <div className="control-group">
            <label htmlFor="direction">Direction</label>

            <select
              id="direction"
              value={direction}
              onChange={handleDirectionChange}
            >
              <option value="asc">Ascending</option>
              <option value="desc">Descending</option>
            </select>
          </div>

          <button
            type="button"
            className="refresh-button"
            onClick={loadEmployees}
            disabled={loading}
          >
            {loading ? "Loading..." : "Refresh"}
          </button>
        </section>

        {successMessage && (
          <div className="alert alert-success">
            {successMessage}
          </div>
        )}

        {error && (
          <div className="alert alert-error">
            <strong>Error:</strong> {error}
          </div>
        )}

        {loading ? (
          <div className="loading-state">
            <div className="spinner"></div>
            <p>Loading employees...</p>
          </div>
        ) : (
          <>
            <EmployeeList
              employees={employees}
              onDelete={handleDelete}
            />

            <Pagination
              pageNumber={pageNumber}
              totalPages={totalPages}
              first={firstPage}
              last={lastPage}
              onPageChange={handlePageChange}
            />
          </>
        )}
      </main>
    </div>
  );
}

export default App;