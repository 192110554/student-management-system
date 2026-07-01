let editingStudentId = null;
let currentPage = 0;
let pageSize = 5;
let totalPages = 0;
let studentsList = [];
let studentChart = null;

async function saveStudent() {

    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const course = document.getElementById("course").value;

    const fileInput = document.getElementById("imageFile");
    const file = fileInput.files[0];

    if (name.trim() === "") {
        showToast("Name is required");
        return;
    }

    if (email.trim() === "") {
        showToast("Email is required");
        return;
    }

    if (course.trim() === "") {
        showToast("Course is required");
        return;
    }

    const emailPattern =
        /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailPattern.test(email)) {
        showToast("Enter valid email");
        return;
    }

    let imageUrl = "";

    if (file) {

        const formData = new FormData();
        formData.append("file", file);

        const uploadResponse = await fetch(
            "http://localhost:8080/api/upload",
            {
                method: "POST",
                body: formData
            });

        imageUrl = await uploadResponse.text();
    }

    const url = editingStudentId
        ? `http://localhost:8080/api/students/${editingStudentId}`
        : "http://localhost:8080/api/students";

    const method =
        editingStudentId ? "PUT" : "POST";

    fetch(url, {

        method: method,

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({

            name: name,
            email: email,
            course: course,
            imageUrl: imageUrl

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

            document.getElementById("name").value = "";
            document.getElementById("email").value = "";
            document.getElementById("course").value = "";
            document.getElementById("imageFile").value = "";

            loadStudents();

        })

        .catch(error => {

            console.log(error);

            showToast("Failed to save student");

        });

}

function loadStudents() {

    document.getElementById("loadingSpinner")
        .style.display = "block";

    fetch(
        `http://localhost:8080/api/students?page=${currentPage}&size=${pageSize}`
    )
        .then(response => response.json())
        .then(data => {

            document.getElementById(
                "loadingSpinner"
            ).style.display = "none";

            console.log(data);

            totalPages = data.totalPages;

            studentsList = data.content;

            document.getElementById("pageInfo").innerText =
                `Page ${currentPage + 1}`;

            const tableBody =
                document.getElementById("studentTableBody");

            tableBody.innerHTML = "";

            const students = data.content;

            updateDashboardCards(students);

            displayStudents(students);

            createChart(students);

        })

.catch(error => {

        console.log(error);

        document.getElementById(
            "loadingSpinner"
        ).style.display = "none";

        showToast("Failed to load students");
    });
}


function deleteStudent(id) {

    if (!confirm("Are you sure you want to delete this student?")) {
        return;
    }

    fetch(`http://localhost:8080/api/students/${id}`, {
        method: "DELETE"
    })
        .then(() => {
            showToast("Student Deleted Successfully!");
            loadStudents();
        })

.catch(error => {

        console.log(error);

        showToast("Failed to delete student");
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
            document.getElementById("imageUrl").value = student.imageUrl;
        })

.catch(error => {

        console.log(error);

        showToast("Student not found");
    });
}

function searchStudent() {

    const name =
        document.getElementById("searchName").value;

    fetch(`http://localhost:8080/api/students/name/${name}`)
        .then(response => response.json())
        .then(data => {

            displayStudents(data);

            updateDashboardCards(data);

        });
}

function searchByEmail() {
    const email =
        document.getElementById("searchEmail").value;

    fetch(
        `http://localhost:8080/api/students/email/${email}`
    )
        .then(response => response.json())
        .then(data => {

            displayStudents(data);

            updateDashboardCards(data);

        })
        .catch(error => {

            console.log(error);
            showToast("Failed to search email");
        });
}

function searchByCourse() {

    const course =
        document.getElementById("searchCourse").value;

    fetch(
        `http://localhost:8080/api/students/course/${course}`
    )
        .then(response => response.json())
        .then(data => {

            displayStudents(data);

            updateDashboardCards(data);

        })
        .catch(error => {

            console.log(error);
            showToast("Failed to search course");
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

function displayStudents(students) {

    const tableBody =
        document.getElementById("studentTableBody");

    tableBody.innerHTML = "";

    students.forEach(student => {

        const row = `
<tr>
    <td>${student.id}</td>
    <td>${student.name}</td>
    <td>${student.email}</td>
    <td>${student.course}</td>

    <td>
        <img src="${student.imageUrl}"
             width="60"
             height="60"
             style="border-radius:50%; object-fit:cover;">
    </td>

    <td>

        <button
    class="btn btn-info btn-sm"
    onclick="viewStudent(${student.id})">

    <i class="fa-solid fa-eye"></i>
    View

</button>

<button
    class="btn btn-warning btn-sm"
    onclick="editStudent(${student.id})">

    <i class="fa-solid fa-pen-to-square"></i>
    Edit

</button>

<button
    class="btn btn-danger btn-sm"
    onclick="deleteStudent(${student.id})">

    <i class="fa-solid fa-trash"></i>
    Delete

</button>

    </td>
</tr>
`;

        tableBody.innerHTML += row;
    });
}

function sortByName() {
    studentsList.sort((a, b) =>
        a.name.localeCompare(b.name)
    );

    displayStudents(studentsList);
}

function sortByIdAsc() {
    studentsList.sort((a, b) =>
        a.id - b.id
    );

    displayStudents(studentsList);
}

function sortByIdDesc() {
    studentsList.sort((a, b) =>
        b.id - a.id
    );

    displayStudents(studentsList);
}

function updateDashboardCards(students) {

    document.getElementById("totalStudents")
        .innerText = students.length;

    const javaCount =
        students.filter(student =>
            student.course.toLowerCase()
                .includes("java")
        ).length;

    document.getElementById("javaStudents")
        .innerText = javaCount;

    const springCount =
        students.filter(student =>
            student.course.toLowerCase()
                .includes("spring")
        ).length;

    document.getElementById("springStudents")
        .innerText = springCount;
}

function toggleTheme() {

    const body = document.getElementById("body");
    const button = document.getElementById("themeBtn");
    const card = document.getElementById("studentCard");
    const table = document.getElementById("studentTable");

    const inputs = document.querySelectorAll("input");

    if (body.classList.contains("bg-light")) {

        body.classList.remove("bg-light");
        body.classList.add("bg-dark", "text-white");

        document.getElementById("studentCard")
            .classList.add("dark-card");

        document.getElementById("studentTable")
            .classList.add("dark-table");

        document.querySelectorAll("input").forEach(input => {
            input.classList.add("dark-input");
        });

        card.classList.add("dark-card");
        table.classList.add("dark-table");

        inputs.forEach(input => {
            input.classList.add("dark-input");
        });

        localStorage.setItem("theme", "dark");

        button.innerHTML = "☀️ Light Mode";

        document.getElementById("chartCard").classList.add("bg-dark");
        document.getElementById("chartCard").classList.remove("bg-white");

        document.getElementById("chartTitle").classList.add("text-white");

    } else {

        body.classList.remove("bg-dark", "text-white");
        body.classList.add("bg-light");

        document.getElementById("studentCard")
            .classList.remove("dark-card");

        document.getElementById("studentTable")
            .classList.remove("dark-table");

        document.querySelectorAll("input").forEach(input => {
            input.classList.remove("dark-input");
        });

        card.classList.remove("dark-card");
        table.classList.remove("dark-table");

        inputs.forEach(input => {
            input.classList.remove("dark-input");
        });

        localStorage.setItem("theme", "light");

        button.innerHTML = "🌙 Dark Mode";

        document.getElementById("chartCard").classList.remove("bg-dark");
        document.getElementById("chartCard").classList.add("bg-white");

        document.getElementById("chartTitle").classList.remove("text-white");
    }
}

window.onload = function () {

    const savedTheme = localStorage.getItem("theme");

    if (savedTheme === "dark") {

        document.getElementById("body")
            .classList.replace("bg-light", "bg-dark");

        document.getElementById("body")
            .classList.add("text-white");

        document.getElementById("themeBtn").innerHTML = "☀️ Light Mode";

        document.getElementById("chartCard").classList.add("bg-dark");
        document.getElementById("chartCard").classList.remove("bg-white");

        document.getElementById("chartTitle").classList.add("text-white");

        document.getElementById("studentCard")
            .classList.add("dark-card");

        document.getElementById("studentTable")
            .classList.add("dark-table");

        document.querySelectorAll("input").forEach(input => {
            input.classList.add("dark-input");
        });
    }

    loadStudents();
};

function createChart(students) {

    const javaCount = students.filter(student =>
        student.course.toLowerCase().includes("java")
    ).length;

    const springCount = students.filter(student =>
        student.course.toLowerCase().includes("spring")
    ).length;

    const totalCount = students.length;

    const ctx = document.getElementById("studentChart");

    if (studentChart) {
        studentChart.destroy();
    }

    studentChart = new Chart(ctx, {

        type: "bar",

        data: {

            labels: [
                "Total",
                "Java",
                "Spring Boot"
            ],

            datasets: [{

                label: "Students",

                data: [
                    totalCount,
                    javaCount,
                    springCount
                ],

                backgroundColor: [
                    "#4e54c8",
                    "#38ef7d",
                    "#ff512f"
                ]

            }]
        },

        options: {

            responsive: true,

            plugins: {

                legend: {

                    display: false

                }

            }

        }

    });

}

function exportStudents() {

    window.location.href =
        "http://localhost:8080/api/students/export";

}

function exportPdf() {

    window.location.href =
        "http://localhost:8080/api/students/export/pdf";

}

function viewStudent(id) {

    fetch(`http://localhost:8080/api/students/${id}`)
        .then(response => response.json())
        .then(student => {

            document.getElementById("modalId").innerText = student.id;
            document.getElementById("modalName").innerText = student.name;
            document.getElementById("modalEmail").innerText = student.email;
            document.getElementById("modalCourse").innerText = student.course;

            document.getElementById("modalImage").src =
                student.imageUrl;

            const modal = new bootstrap.Modal(
                document.getElementById("studentModal")
            );

            modal.show();

        })
        .catch(error => {

            console.log(error);

            showToast("Unable to load student.");

        });

}