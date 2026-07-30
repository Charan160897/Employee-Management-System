function Pagination({
  pageNumber,
  totalPages,
  first,
  last,
  onPageChange,
}) {
  if (totalPages === 0) {
    return null;
  }

  return (
    <div className="pagination">
      <button
        type="button"
        className="pagination-button"
        disabled={first}
        onClick={() => onPageChange(pageNumber - 1)}
      >
        Previous
      </button>

      <span className="page-information">
        Page {pageNumber + 1} of {totalPages}
      </span>

      <button
        type="button"
        className="pagination-button"
        disabled={last}
        onClick={() => onPageChange(pageNumber + 1)}
      >
        Next
      </button>
    </div>
  );
}

export default Pagination;