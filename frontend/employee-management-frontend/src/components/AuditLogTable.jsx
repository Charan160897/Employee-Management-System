function AuditLogTable({
  logs,
  loading,
  error,
  onRefresh,
}) {

  const formatTimestamp = (timestamp) => {
    if (!timestamp) {
      return "—";
    }

    return new Date(
      timestamp
    ).toLocaleString(
      "en-US",
      {
        dateStyle: "medium",
        timeStyle: "short",
      }
    );
  };

  const getActionClass = (action) => {
    switch (action) {
      case "CREATE":
        return "audit-badge audit-create";

      case "UPDATE":
        return "audit-badge audit-update";

      case "DELETE":
        return "audit-badge audit-delete";

      default:
        return "audit-badge";
    }
  };

  if (loading) {
    return (
      <section className="audit-section">
        <p>
          Loading audit logs...
        </p>
      </section>
    );
  }

  if (error) {
    return (
      <section className="audit-section">
        <div className="alert alert-error">
          {error}
        </div>

        <button
          type="button"
          className="refresh-button"
          onClick={onRefresh}
        >
          Retry
        </button>
      </section>
    );
  }

  return (
    <section className="audit-section">

      <div className="audit-header">
        <div>
          <h2>
            Audit Logs
          </h2>

          <p>
            View recent employee management
            activity.
          </p>
        </div>

        <button
          type="button"
          className="refresh-button"
          onClick={onRefresh}
        >
          Refresh Logs
        </button>
      </div>

      {logs.length === 0 ? (
        <div className="audit-empty">
          No audit logs are available.
        </div>
      ) : (
        <div className="table-container">

          <table className="audit-table">

            <thead>
              <tr>
                <th>ID</th>
                <th>Time</th>
                <th>User</th>
                <th>Action</th>
                <th>Employee</th>
              </tr>
            </thead>

            <tbody>
              {logs.map((log) => (
                <tr key={log.id}>

                  <td>
                    {log.id}
                  </td>

                  <td>
                    {formatTimestamp(
                      log.timestamp
                    )}
                  </td>

                  <td>
                    {log.username}
                  </td>

                  <td>
                    <span
                      className={
                        getActionClass(
                          log.action
                        )
                      }
                    >
                      {log.action}
                    </span>
                  </td>

                  <td>
                    {log.employeeName}
                  </td>

                </tr>
              ))}
            </tbody>

          </table>

        </div>
      )}

    </section>
  );
}

export default AuditLogTable;