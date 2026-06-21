let editingStudentId = null;
let currentPage = 0;
let pageSize = 5;
let totalPages = 0;
function saveStudent() {

    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const course = document.getElementById("course").value;

    const url = editingStudentId
        ? `http://localhost:8080/api/students/${editingStudentId}`
        : "http://localhost:8080/api/students";

    const method = editingStudentId ? "PUT" : "POST";

    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            name: name,
            email: email,
            course: course
        })
    })
        .then(response => response.json())
        .then(data => {

            if (editingStudentId) {
                showToast("Student Updated Successfully!");
            } else {
                showToast("Student Saved Successfully!");
            }

            editingStudentId = null;

            console.log(data);

            document.getElementById("name").value = "";
            document.getElementById("email").value = "";
            document.getElementById("course").value = "";

            loadStudents();
        });
}

function loadStudents() {

    fetch(
        `http://localhost:8080/api/students?page=${currentPage}&size=${pageSize}`
    )
        .then(response => response.json())
        .then(data => {

        console.log(data);

            totalPages = data.totalPages;

            document.getElementById("pageInfo").innerText =
                `Page ${currentPage + 1}`;

        const tableBody =
            document.getElementById("studentTableBody");

        tableBody.innerHTML = "";

        data.content.forEach(student => {

            const row = `
    <tr>
        <td>${student.id}</td>
        <td>${student.name}</td>
        <td>${student.email}</td>
        <td>${student.course}</td>
        <td>
    <button
        class="btn btn-warning btn-sm"
        onclick="editStudent(${student.id})">
    Edit
</button>

<button
        class="btn btn-danger btn-sm"
        onclick="deleteStudent(${student.id})">
    Delete
</button>
</td>
    </tr>
`;

            tableBody.innerHTML += row;
        });

    });
}

function deleteStudent(id) {

    fetch(`http://localhost:8080/api/students/${id}`, {
        method: "DELETE"
    })
        .then(() => {
            showToast("Student Deleted Successfully!");
            loadStudents();
        });
}

function editStudent(id) {

    editingStudentId = id;

    console.log("Edit clicked for ID:", id);

    fetch(`http://localhost:8080/api/students/${id}`)
        .then(response => response.json())
        .then(student => {

            console.log("Student received:", student);

            document.getElementById("name").value = student.name;
            document.getElementById("email").value = student.email;
            document.getElementById("course").value = student.course;
        });
}

function searchStudent() {

    const name =
        document.getElementById("searchName").value;

    fetch(`http://localhost:8080/api/students/name/${name}`)
        .then(response => response.json())
        .then(data => {

            const tableBody =
                document.getElementById("studentTableBody");

            tableBody.innerHTML = "";

            data.forEach(student => {

                const row = `
                <tr>
                    <td>${student.id}</td>
                    <td>${student.name}</td>
                    <td>${student.email}</td>
                    <td>${student.course}</td>
                    <td>
                        <button
        class="btn btn-warning btn-sm"
        onclick="editStudent(${student.id})">
    Edit
</button>

<button
        class="btn btn-danger btn-sm"
        onclick="deleteStudent(${student.id})">
    Delete
</button>
                    </td>
                </tr>
                `;

                tableBody.innerHTML += row;
            });

        });
}

function previousPage() {

    if (currentPage > 0) {
        currentPage--;
        loadStudents();
    }
}

function nextPage() {

    if (currentPage < totalPages - 1) {
        currentPage++;
        loadStudents();
    }
}

function showToast(message) {

    document.getElementById("toastMessage")
        .innerText = message;

    const toast =
        new bootstrap.Toast(
            document.getElementById("successToast")
        );

    toast.show();
}

loadStudents();