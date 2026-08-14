import {
  fireEvent,
  render,
  screen,
} from "@testing-library/react";

import EmployeeList from "./EmployeeList";

const employees = [
  {
    id: 1,
    firstName: "Rahul",
    lastName: "Kumar",
    fullName: "Rahul Kumar",
    email: "rahul@example.com",
    department: "Engineering",
    jobTitle: "Backend Developer",
    salary: 90000,
    hireDate: "2026-07-28",
    active: true,
  },
];

describe("EmployeeList", () => {
  test("renders employee information", () => {
    render(
      <EmployeeList
        employees={employees}
        onView={() => {}}
        onEdit={() => {}}
        onDelete={() => {}}
      />
    );

    expect(
      screen.getByText("Rahul Kumar")
    ).toBeInTheDocument();

    expect(
      screen.getByText("Engineering")
    ).toBeInTheDocument();

    expect(
      screen.getByText("Backend Developer")
    ).toBeInTheDocument();
  });

  test("calls onView when View is clicked", () => {
  const onView = vi.fn();

  render(
    <EmployeeList
      employees={employees}
      onView={onView}
      onEdit={() => {}}
      onDelete={() => {}}
    />
  );

  fireEvent.click(
    screen.getByRole(
      "button",
      { name: "View" }
    )
  );

  expect(onView)
    .toHaveBeenCalledTimes(1);

  expect(onView)
    .toHaveBeenCalledWith(
      employees[0]
    );
  });

  test("calls onEdit when Edit is clicked", () => {
  const onEdit = vi.fn();

  render(
    <EmployeeList
      employees={employees}
      onView={() => {}}
      onEdit={onEdit}
      onDelete={() => {}}
      isAdmin={true}
    />
  );

  fireEvent.click(
    screen.getByRole(
      "button",
      { name: "Edit" }
    )
  );

  expect(onEdit)
    .toHaveBeenCalledWith(
      employees[0]
    );
   });

   test("calls onDelete when Delete is clicked", () => {
  const onDelete = vi.fn();

  render(
    <EmployeeList
      employees={employees}
      onView={() => {}}
      onEdit={() => {}}
      onDelete={onDelete}
      isAdmin={true}
    />
  );

  fireEvent.click(
    screen.getByRole(
      "button",
      { name: "Delete" }
    )
  );

  expect(onDelete)
    .toHaveBeenCalledWith(
      employees[0]
    );
  });

  test("shows empty state when no employees exist", () => {
  render(
    <EmployeeList
      employees={[]}
      onView={() => {}}
      onEdit={() => {}}
      onDelete={() => {}}
    />
  );

  expect(
    screen.getByText(
      "No employees found"
    )
  ).toBeInTheDocument();
 });
});