import {
  useCallback,
  useEffect,
  useState,
} from "react";

import "./App.css";

import {
  createEmployee,
  deleteEmployee,
  filterEmployees,
  updateEmployee,
} from "./api/employeeService";

import EmployeeFilters from "./components/EmployeeFilters";
import EmployeeForm from "./components/EmployeeForm";
import EmployeeList from "./components/EmployeeList";
import Header from "./components/Header";
import Pagination from "./components/Pagination";

function App() {
  const [employees, setEmployees] =
    useState([]);

  const [pageNumber, setPageNumber] =
    useState(0);

  const [pageSize, setPageSize] =
    useState(5);

  const [totalElements, setTotalElements] =
    useState(0);

  const [totalPages, setTotalPages] =
    useState(0);
  
  const [searchKeyword, setsearchKeyword] =
    useState("");

  const [departmentFilter, setDepartmentFilter] =
    useState("");

  const [statusFilter, setStatusFilter] =
    useState("all");

  const [firstPage, setFirstPage] =
    useState(true);

  const [lastPage, setLastPage] =
    useState(true);

  const [sortBy, setSortBy] =
    useState("id");

  const [direction, setDirection] =
    useState("asc");

  const [loading, setLoading] =
    useState(true);

  const [submitting, setSubmitting] =
    useState(false);

  const [error, setError] =
    useState("");

  const [
    successMessage,
    setSuccessMessage,
  ] = useState("");

  const [
    serverValidationErrors,
    setServerValidationErrors,
  ] = useState({});

  const [showEmployeeForm, setShowEmployeeForm] =
    useState(false);

  const [editingEmployee, setEditingEmployee] =
    useState(null);

 const loadEmployees = useCallback(
  async () => {
    try {
      setLoading(true);
      setError("");

      let activeValue = null;

      if (statusFilter === "active") {
        activeValue = true;
      } else if (
        statusFilter === "inactive"
      ) {
        activeValue = false;
      }

      const data = await filterEmployees({
        keyword: searchKeyword,
        department: departmentFilter,
        active: activeValue,
        page: pageNumber,
        size: pageSize,
        sortBy,
        direction,
      });

      setEmployees(data.content || []);

      setPageNumber(
        data.pageNumber ?? 0
      );

      setPageSize(
        data.pageSize ?? pageSize
      );

      setTotalElements(
        data.totalElements ?? 0
      );

      setTotalPages(
        data.totalPages ?? 0
      );

      setFirstPage(
        data.first ?? true
      );

      setLastPage(
        data.last ?? true
      );
    } catch (requestError) {
      console.error(
        "Failed to load employees:",
        requestError
      );

      if (
        requestError.code ===
        "ECONNABORTED"
      ) {
        setError(
          "The request timed out. Please try again."
        );
      } else if (
        !requestError.response
      ) {
        setError(
          "Unable to connect to the backend. Make sure Spring Boot is running on port 8080."
        );
      } else {
        setError(
          requestError.response.data
            ?.message ||
            "Unable to load employees."
        );
      }
    } finally {
      setLoading(false);
    }
  },
  [
    pageNumber,
    pageSize,
    sortBy,
    direction,
    searchKeyword,
    departmentFilter,
    statusFilter,
  ]
);

  useEffect(() => {
    loadEmployees();
  }, [loadEmployees]);

  const clearMessages = () => {
    setError("");
    setSuccessMessage("");
    setServerValidationErrors({});
  };

  const handleOpenCreateForm = () => {
    clearMessages();
    setEditingEmployee(null);
    setShowEmployeeForm(true);
  };

  const handleEditEmployee = (employee) => {
    clearMessages();
    setEditingEmployee(employee);
    setShowEmployeeForm(true);

    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  };

  const handleApplyFilters = ({
    keyword,
    department,
    status,
  }) => {
    clearMessages();
    setsearchKeyword(keyword);
    setDepartmentFilter(department);
    setStatusFilter(status);
    setPageNumber(0);
  };

  const handleClearFilters = () => {
    clearMessages();
    setsearchKeyword("");
    setDepartmentFilter("");
    setStatusFilter("all");
    setPageNumber(0);
  };
  const getActiveFilterDescription = () => {
  const activeFilters = [];

  if (searchKeyword) {
    activeFilters.push(
      `Search: "${searchKeyword}"`
    );
  }

  if (departmentFilter) {
    activeFilters.push(
      `Department: ${departmentFilter}`
    );
  }

  if (statusFilter === "active") {
    activeFilters.push(
      "Status: Active"
    );
  }

  if (
    statusFilter === "inactive"
  ) {
    activeFilters.push(
      "Status: Inactive"
    );
  }

  if (activeFilters.length === 0) {
    return "All employees";
  }

  return activeFilters.join(" • ");
};

  const handleCloseEmployeeForm = () => {
    if (submitting) {
      return;
    }

    setShowEmployeeForm(false);
    setEditingEmployee(null);
    setServerValidationErrors({});
  };

  const handleCreateEmployee = async (
    employeeData
  ) => {
    try {
      setSubmitting(true);
      clearMessages();

      const createdEmployee =
        await createEmployee(employeeData);

      setSuccessMessage(
        `${createdEmployee.fullName} was created successfully.`
      );

      setShowEmployeeForm(false);
      setEditingEmployee(null);

      setSortBy("id");
      setDirection("desc");

      if (pageNumber !== 0) {
        setPageNumber(0);
      } else {
        await loadEmployees();
      }

      return true;
    } catch (requestError) {
      handleSaveError(
        requestError,
        "create"
      );

      return false;
    } finally {
      setSubmitting(false);
    }
  };

  const handleUpdateEmployee = async (
    employeeData
  ) => {
    if (!editingEmployee?.id) {
      setError(
        "No employee was selected for editing."
      );

      return false;
    }

    try {
      setSubmitting(true);
      clearMessages();

      const updatedEmployee =
        await updateEmployee(
          editingEmployee.id,
          employeeData
        );

      setSuccessMessage(
        `${updatedEmployee.fullName} was updated successfully.`
      );

      setShowEmployeeForm(false);
      setEditingEmployee(null);

      await loadEmployees();

      return true;
    } catch (requestError) {
      handleSaveError(
        requestError,
        "update"
      );

      return false;
    } finally {
      setSubmitting(false);
    }
  };

  const handleSaveError = (
    requestError,
    operation
  ) => {
    console.error(
      `Failed to ${operation} employee:`,
      requestError
    );

    const status =
      requestError.response?.status;

    const responseData =
      requestError.response?.data;

    if (!requestError.response) {
      setError(
        "Unable to connect to the backend. Make sure Spring Boot is running."
      );

      return;
    }

    if (status === 400) {
      setServerValidationErrors(
        responseData?.validationErrors || {}
      );

      setError(
        responseData?.message ||
          "Please correct the highlighted fields."
      );

      return;
    }

    if (status === 404) {
      setError(
        responseData?.message ||
          "The employee no longer exists."
      );

      return;
    }

    if (status === 409) {
      const duplicateMessage =
        responseData?.message ||
        "An employee with this email already exists.";

      setError(duplicateMessage);

      setServerValidationErrors({
        email: duplicateMessage,
      });

      return;
    }

    setError(
      responseData?.message ||
        `Unable to ${operation} the employee.`
    );
  };

  const handleEmployeeFormSubmit = async (
    employeeData
  ) => {
    if (editingEmployee) {
      return handleUpdateEmployee(
        employeeData
      );
    }

    return handleCreateEmployee(
      employeeData
    );
  };

  const handlePageChange = (newPage) => {
    if (
      newPage >= 0 &&
      newPage < totalPages
    ) {
      setPageNumber(newPage);
    }
  };

  const handlePageSizeChange = (event) => {
    setPageSize(
      Number(event.target.value)
    );

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
    const employeeName =
      employee.fullName ||
      `${employee.firstName} ${employee.lastName}`;

    const confirmed = window.confirm(
      `Are you sure you want to delete ${employeeName}?`
    );

    if (!confirmed) {
      return;
    }

    try {
      clearMessages();

      await deleteEmployee(employee.id);

      setSuccessMessage(
        `${employeeName} was deleted successfully.`
      );

      if (
        editingEmployee?.id === employee.id
      ) {
        setEditingEmployee(null);
        setShowEmployeeForm(false);
      }

      if (
        employees.length === 1 &&
        pageNumber > 0
      ) {
        setPageNumber(
          (currentPage) =>
            currentPage - 1
        );
      } else {
        await loadEmployees();
      }
    } catch (requestError) {
      console.error(
        "Failed to delete employee:",
        requestError
      );

      setError(
        requestError.response?.data
          ?.message ||
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
              Create, view, update and delete
              employee records.
            </p>
          </div>

          <div className="summary-actions">
            <div className="employee-count">
              <span>Total employees</span>
              <strong>{totalElements}</strong>
            </div>

            <button
              type="button"
              className="add-employee-button"
              onClick={handleOpenCreateForm}
              disabled={submitting}
            >
              + Add Employee
            </button>
          </div>
        </section>

        {successMessage && (
          <div className="alert alert-success">
            {successMessage}
          </div>
        )}

        {error && (
          <div className="alert alert-error">
            <strong>Error:</strong>{" "}
            {error}
          </div>
        )}

        <EmployeeFilters
          initialKeyword={searchKeyword}
          initialDepartment={departmentFilter}
          initialStatus={statusFilter}
          onApplyFilters={handleApplyFilters}
          onClearFilters={handleClearFilters}
         disabled={loading || submitting}
        />

        {showEmployeeForm && (
          <EmployeeForm
            employee={editingEmployee}
            onSubmit={
              handleEmployeeFormSubmit
            }
            onCancel={
              handleCloseEmployeeForm
            }
            submitting={submitting}
            serverErrors={
              serverValidationErrors
            }
          />
        )}
        <div className="active-filter-summary">
          <span>Current view:</span>
          <strong>
            {getActiveFilterDescription()}
           </strong>
        </div>

        <section className="controls-section">
          <div className="control-group">
            <label htmlFor="pageSize">
              Rows per page
            </label>

            <select
              id="pageSize"
              value={pageSize}
              onChange={
                handlePageSizeChange
              }
            >
              <option value={5}>5</option>
              <option value={10}>10</option>
              <option value={20}>20</option>
            </select>
          </div>

          <div className="control-group">
            <label htmlFor="sortBy">
              Sort by
            </label>

            <select
              id="sortBy"
              value={sortBy}
              onChange={handleSortChange}
            >
              <option value="id">ID</option>
              <option value="firstName">
                First name
              </option>
              <option value="lastName">
                Last name
              </option>
              <option value="department">
                Department
              </option>
              <option value="jobTitle">
                Job title
              </option>
              <option value="salary">
                Salary
              </option>
              <option value="hireDate">
                Hire date
              </option>
            </select>
          </div>

          <div className="control-group">
            <label htmlFor="direction">
              Direction
            </label>

            <select
              id="direction"
              value={direction}
              onChange={
                handleDirectionChange
              }
            >
              <option value="asc">
                Ascending
              </option>
              <option value="desc">
                Descending
              </option>
            </select>
          </div>

          <button
            type="button"
            className="refresh-button"
            onClick={loadEmployees}
            disabled={loading}
          >
            {loading
              ? "Loading..."
              : "Refresh"}
          </button>
        </section>

        {loading ? (
          <div className="loading-state">
            <div className="spinner"></div>
            <p>Loading employees...</p>
          </div>
        ) : (
          <>
            <EmployeeList
              employees={employees}
              onEdit={
                handleEditEmployee
              }
              onDelete={handleDelete}
            />

            <Pagination
              pageNumber={pageNumber}
              totalPages={totalPages}
              first={firstPage}
              last={lastPage}
              onPageChange={
                handlePageChange
              }
            />
          </>
        )}
      </main>
    </div>
  );
}

export default App;