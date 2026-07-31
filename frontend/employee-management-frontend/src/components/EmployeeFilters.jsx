import { useState } from "react";

function EmployeeFilters({
  initialKeyword = "",
  initialDepartment = "",
  initialStatus = "all",
  onApplyFilters,
  onClearFilters,
  disabled,
}) {
  const [keyword, setKeyword] =
    useState(initialKeyword);

  const [department, setDepartment] =
    useState(initialDepartment);

  const [status, setStatus] =
    useState(initialStatus);

  const handleSubmit = (event) => {
    event.preventDefault();

    onApplyFilters({
      keyword: keyword.trim(),
      department,
      status,
    });
  };

  const handleClear = () => {
    setKeyword("");
    setDepartment("");
    setStatus("all");

    onClearFilters();
  };

  return (
    <section className="filters-card">
      <div className="filters-header">
        <div>
          <h2>Search and Filters</h2>
          <p>
            Find employees by name, email,
            department or active status.
          </p>
        </div>
      </div>

      <form
        className="filters-form"
        onSubmit={handleSubmit}
      >
        <div className="filter-field filter-search">
          <label htmlFor="employeeKeyword">
            Search
          </label>

          <input
            id="employeeKeyword"
            type="search"
            value={keyword}
            onChange={(event) =>
              setKeyword(event.target.value)
            }
            placeholder="Search name or email"
            disabled={disabled}
          />
        </div>

        <div className="filter-field">
          <label htmlFor="departmentFilter">
            Department
          </label>

          <select
            id="departmentFilter"
            value={department}
            onChange={(event) =>
              setDepartment(event.target.value)
            }
            disabled={disabled}
          >
            <option value="">
              All departments
            </option>

            <option value="Engineering">
              Engineering
            </option>

            <option value="Human Resources">
              Human Resources
            </option>

            <option value="Finance">
              Finance
            </option>

            <option value="Sales">
              Sales
            </option>

            <option value="Marketing">
              Marketing
            </option>

            <option value="Operations">
              Operations
            </option>

            <option value="Information Technology">
              Information Technology
            </option>
          </select>
        </div>

        <div className="filter-field">
          <label htmlFor="statusFilter">
            Status
          </label>

          <select
            id="statusFilter"
            value={status}
            onChange={(event) =>
              setStatus(event.target.value)
            }
            disabled={disabled}
          >
            <option value="all">
              All employees
            </option>

            <option value="active">
              Active only
            </option>

            <option value="inactive">
              Inactive only
            </option>
          </select>
        </div>

        <p className="filter-note">
           When multiple filters are selected, search is applied first,
           followed by department and status.
        </p>

        <div className="filter-actions">
          <button
            type="submit"
            className="apply-filter-button"
            disabled={disabled}
          >
            Apply
          </button>

          <button
            type="button"
            className="clear-filter-button"
            onClick={handleClear}
            disabled={disabled}
          >
            Clear
          </button>
        </div>
      </form>
    </section>
  );
}

export default EmployeeFilters;