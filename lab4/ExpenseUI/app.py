from flask import Flask, render_template, request, redirect, session, url_for
import requests

app = Flask(__name__)
app.secret_key = "super-secret-key"

SPRING_API_URL = "http://localhost:8080"


@app.route("/")
def home():
    return redirect(url_for("login"))


@app.route("/login", methods=["GET", "POST"])
def login():
    if request.method == "POST":
        username = request.form["username"]
        password = request.form["password"]

        response = requests.post(
            f"{SPRING_API_URL}/auth/login",
            json={
                "username": username,
                "password": password
            }
        )

        if response.status_code == 200:
            user_response = requests.get(f"{SPRING_API_URL}/user/username/{username}")

            if user_response.status_code == 200:
                user_data = user_response.json()
                session["user_id"] = user_data["id"]
                session["username"] = user_data["username"]
                return redirect(url_for("dashboard"))

            return render_template("login.html", error="Could not load user data")

        return render_template("login.html", error="Invalid username or password")

    return render_template("login.html")


@app.route("/register", methods=["GET", "POST"])
def register():
    if request.method == "POST":
        username = request.form["username"]
        password = request.form["password"]

        response = requests.post(
            f"{SPRING_API_URL}/auth/register",
            json={
                "username": username,
                "password": password
            }
        )

        if response.status_code == 201:
            return redirect(url_for("login"))

        return render_template("register.html", error="Register failed")

    return render_template("register.html")


@app.route("/dashboard")
def dashboard():
    if "user_id" not in session:
        return redirect(url_for("login"))

    return render_template("dashboard.html", username=session["username"])


@app.route("/expenses")
def expenses():
    if "user_id" not in session:
        return redirect(url_for("login"))

    user_id = session["user_id"]

    response = requests.get(f"{SPRING_API_URL}/users/{user_id}/expense")

    if response.status_code == 200:
        expenses_list = response.json()
    else:
        expenses_list = []

    return render_template("expenses.html", expenses=expenses_list)


@app.route("/add-expense", methods=["GET", "POST"])
def add_expense():
    if "user_id" not in session:
        return redirect(url_for("login"))

    user_id = session["user_id"]

    if request.method == "POST":
        amount = float(request.form["amount"])
        category = request.form["category"]
        description = request.form["description"]

        response = requests.post(
            f"{SPRING_API_URL}/users/{user_id}/expense",
            json={
                "id": 0,
                "amount": amount,
                "category": category,
                "description": description,
                "userId": user_id
            }
        )

        if response.status_code == 201:
            return redirect(url_for("expenses"))

        return render_template("add_expense.html", error="Could not add expense")

    return render_template("add_expense.html")


@app.route("/search-expense", methods=["GET", "POST"])
def search_expense():
    if "user_id" not in session:
        return redirect(url_for("login"))

    user_id = session["user_id"]

    if request.method == "POST":
        category = request.form["category"]

        response = requests.get(f"{SPRING_API_URL}/users/{user_id}/expense/search/{category}")

        if response.status_code == 200:
            expenses_list = response.json()
        else:
            expenses_list = []

        return render_template("search_expense.html", expenses=expenses_list, searched=True)

    return render_template("search_expense.html", expenses=[], searched=False)


@app.route("/delete-expense/<int:expense_id>", methods=["POST"])
def delete_expense(expense_id):
    if "user_id" not in session:
        return redirect(url_for("login"))

    user_id = session["user_id"]

    requests.delete(f"{SPRING_API_URL}/users/{user_id}/expense/{expense_id}")

    return redirect(url_for("expenses"))


@app.route("/update-expense/<int:expense_id>", methods=["GET", "POST"])
def update_expense(expense_id):
    if "user_id" not in session:
        return redirect(url_for("login"))

    user_id = session["user_id"]

    if request.method == "POST":
        amount = float(request.form["amount"])
        category = request.form["category"]
        description = request.form["description"]

        patch_body = [
            {"op": "replace", "path": "/amount", "value": amount},
            {"op": "replace", "path": "/category", "value": category},
            {"op": "replace", "path": "/description", "value": description}
        ]

        response = requests.patch(
            f"{SPRING_API_URL}/users/{user_id}/expense/{expense_id}",
            json=patch_body,
            headers={"Content-Type": "application/json-patch+json"}
        )

        if response.status_code in [200, 204]:
            return redirect(url_for("expenses"))

        return render_template("update_expense.html", error="Could not update expense", expense=None)

    response = requests.get(f"{SPRING_API_URL}/users/{user_id}/expense/{expense_id}")

    if response.status_code != 200:
        return redirect(url_for("expenses"))

    expense = response.json()
    return render_template("update_expense.html", expense=expense)


@app.route("/logout")
def logout():
    session.clear()
    return redirect(url_for("login"))


if __name__ == "__main__":
    app.run(debug=True)