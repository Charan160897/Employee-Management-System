function EmployeeDashboard({
  statistics,
  loading,
  error,
  onRefresh,
}) {
  if (loading && !statistics) {
    return (
      <section className="dashboard-loading">
        <div className="spinner"></div>
        <p>Loading employee statistics...</p>
      </section>
    );
  }

  if (error && !statistics) {
    return (
      <section className="dashboard-error">
        <p>{error}</p>

        <button
          type="button"
          className="refresh-button"
          onClick={onRefresh}
        >
          Retry Dashboard
        </button>
      </section>
    );
  }

  if (!statistics) {
    return null;
  }

  const {
    totalEmployees = 0,
    activeEmployees = 0,
    inactiveEmployees = 0,
    averageSalary = 0,
    totalDepartments = 0,
    departmentCounts = [],
  } = statistics;

  const highestDepartmentCount =
    departmentCounts.length > 0
      ? Math.max(
          ...departmentCounts.map(
            (item) => item.employeeCount
          )
        )
      : 0;

  const formatCurrency = (value) =>
    Number(value || 0).toLocaleString(
      "en-US",
      {
        style: "currency",
        currency: "USD",
        maximumFractionDigits: 2,
      }
    );

  return (
    <section className="employee-dashboard">
      <div className="dashboard-header">
        <div>
          <h2>Employee Dashboard</h2>

          <p>
            Organization-wide statistics from the
            employee database.
          </p>
        </div>

        <button
          type="button"
          className="dashboard-refresh-button"
          onClick={onRefresh}
          disabled={loading}
        >
          {loading
            ? "Refreshing..."
            : "Refresh Dashboard"}
        </button>
      </div>

      <div className="statistics-grid">
        <article className="statistic-card">
          <span className="statistic-label">
            Total Employees
          </span>

          <strong className="statistic-value">
            {totalEmployees}
          </strong>

          <small>
            All employee records
          </small>
        </article>

        <article className="statistic-card">
          <span className="statistic-label">
            Active Employees
          </span>

          <strong className="statistic-value">
            {activeEmployees}
          </strong>

          <small>
            Currently active
          </small>
        </article>

        <article className="statistic-card">
          <span className="statistic-label">
            Inactive Employees
          </span>

          <strong className="statistic-value">
            {inactiveEmployees}
          </strong>

          <small>
            Currently inactive
          </small>
        </article>

        <article className="statistic-card">
          <span className="statistic-label">
            Average Salary
          </span>

          <strong className="statistic-value statistic-currency">
            {formatCurrency(averageSalary)}
          </strong>

          <small>
            Across all employees
          </small>
        </article>

        <article className="statistic-card">
          <span className="statistic-label">
            Departments
          </span>

          <strong className="statistic-value">
            {totalDepartments}
          </strong>

          <small>
            Distinct departments
          </small>
        </article>
      </div>

      <div className="department-summary-card">
        <div className="department-summary-header">
          <div>
            <h3>Employees by Department</h3>

            <p>
              Number of employees assigned to each
              department.
            </p>
          </div>
        </div>

        {departmentCounts.length === 0 ? (
          <div className="dashboard-empty-state">
            No department statistics are available.
          </div>
        ) : (
          <div className="department-bars">
            {departmentCounts.map((item) => {
              const barPercentage =
                highestDepartmentCount > 0
                  ? (
                      item.employeeCount /
                      highestDepartmentCount
                    ) * 100
                  : 0;

              return (
                <div
                  className="department-bar-row"
                  key={item.department}
                >
                  <div className="department-bar-label">
                    <span>{item.department}</span>

                    <strong>
                      {item.employeeCount}
                    </strong>
                  </div>

                  <div className="department-bar-track">
                    <div
                      className="department-bar-fill"
                      style={{
                        width: `${barPercentage}%`,
                      }}
                    ></div>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>
    </section>
  );
}

export default EmployeeDashboard;