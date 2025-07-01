# Jenkins Pipeline vs. Multibranch Pipeline

## 🧱 Overview

| Feature                        | Pipeline Job                                         | Multibranch Pipeline Job                                  |
|-------------------------------|------------------------------------------------------|------------------------------------------------------------|
| **Purpose**                   | Build one pipeline (usually one branch)              | Auto-discover and build multiple branches (or PRs)         |
| **Triggers**                  | Manual, SCM polling, or webhook (manually setup)     | Auto-triggered by branch/PR events via webhook             |
| **Branch Awareness**          | Not branch-aware                                     | Branch-aware (builds per branch/PR)                        |
| **Use Case**                  | Single project/branch, quick setup                   | Multiple branches, team-based workflows                    |
| **Job Configuration**         | Created via “New Item → Pipeline”                    | Created via “New Item → Multibranch Pipeline”              |
| **Job Structure**             | One job = one branch                                 | One job = folder with sub-jobs for each branch             |
| **Build History**             | One linear history                                   | Separate histories per branch                              |
| **Requires Jenkinsfile?**     | Optional (inline or in repo)                         | Mandatory in each branch                                   |
| **UI Setup**                  | Manually define Git + pipeline script                | Just add GitHub source; rest is automatic                  |
| **Branch Environment Vars**   | Limited (e.g., `BRANCH_NAME` not guaranteed)         | Full support (`BRANCH_NAME`, `CHANGE_ID`, etc.)            |

---

## 🔁 When to Use Which?

### ✅ Use **Pipeline Job** when:
- You're building only one branch (e.g., `main`)
- You want a quick/simple setup
- You want to define the pipeline **inline**
- You don't need automatic handling of PRs or feature branches

### ✅ Use **Multibranch Pipeline Job** when:
- You work with **multiple branches** or PRs
- You want Jenkins to **automatically detect and build branches**
- You want environment variables like `BRANCH_NAME`, `CHANGE_ID`
- You need separate build history and logs per branch

---

## 🧪 Jenkinsfile Differences

### ✅ Pipeline Job Example (Inline or from SCM)

```groovy
pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo 'Building...'
      }
    }
  }
}

# ✅ Multibranch Pipeline Example

Each branch in your GitHub repo **must** include a `Jenkinsfile` like:

```groovy
pipeline {
  agent any
  stages {
    stage('Info') {
      steps {
        echo "Running on branch: ${env.BRANCH_NAME}"
      }
    }
  }
}

## 📌 Feature Comparison

| Feature                          | Pipeline Job           | Multibranch Pipeline        |
|----------------------------------|------------------------|------------------------------|
| **GitHub Webhook Support**       | ✅ Manual setup        | ✅ Automatically supported   |
| **Multiple branch support**      | ❌ Single branch       | ✅ Multiple branches detected|
| **Auto-create jobs per branch**  | ❌ No                  | ✅ Yes                       |
| **Inline Jenkinsfile**           | ✅ Supported           | ❌ Not allowed               |
| **Access to `env.BRANCH_NAME`**  | ❌ Unreliable          | ✅ Always available          |
| **Good for**                     | Simple CI/CD setups    | Modern, team-based workflows |

---

## ✅ Summary

| Use Case                                 | Recommended Job Type       |
|------------------------------------------|----------------------------|
| One branch, fast setup                   | ✅ Pipeline Job            |
| Feature branches, PRs, team dev workflow | ✅ Multibranch Pipeline    |
| Access to GitHub-specific vars           | ✅ Multibranch Pipeline    |
| Manual script definition in UI           | ✅ Pipeline Job            |



# ✅ Jenkins Shared Library – Everything You Should Know

Jenkins Shared Libraries are a powerful way to **re-use**, **organize**, and **version** your pipeline logic across multiple projects.

---

## 📁 Recommended Shared Library Repo Structure

```
jenkins-shared-library/
├── vars/
│   ├── buildJar.groovy          # Global pipeline functions
│   └── deployApp.groovy
├── src/
│   └── org/example/MyHelper.groovy  # Helper classes
├── resources/
│   └── templates/email.html     # Templates/scripts
└── README.md
```

| Folder       | Purpose                                                                 |
|--------------|-------------------------------------------------------------------------|
| `vars/`      | Defines **global functions** callable in Jenkinsfiles (`buildJar()`)    |
| `src/`       | Contains **Groovy classes** to support logic inside `vars/`             |
| `resources/` | Static files or templates accessed via `libraryResource()`              |

---

## 🧠 How Global Vars Work

Each file in `vars/` defines a **single global step**.

**Example: `vars/buildJar.groovy`**

```groovy
def call() {
    echo "Building the JAR file..."
    sh 'mvn clean package'
}
```

**Usage in Jenkinsfile:**

```groovy
buildJar()
```

⚠️ One file = One step. Don’t define multiple functions per file.

---

## 🪛 Debugging Tips

- Use `echo` or `println` in library code to trace execution.
- `MissingMethodException`? Check:
  - Filename matches the function (`buildJar.groovy` → `buildJar()`)
  - Syntax or script loading errors

---

## 🏷 Versioning Shared Libraries

Use versioned references for stability:

```groovy
@Library('jenkins-shared-library@v1.2.3') _
```

- Define Git tags or version branches in your shared library repo.
- Pipelines can stick to stable versions while you update newer ones.

---

## 🧩 Best Practices for Maintainability

- Move business logic to `src/`
- Keep `vars/` as entry points
- Don’t clutter with project-specific code
- Write small, testable, reusable steps

---

## 📨 Use `resources/` Folder

```groovy
def content = libraryResource 'templates/email.html'
```

Useful for:
- Email templates
- Shell scripts
- Static config files

---

## ⚠️ Use `@NonCPS` When Needed

If your function uses:
- Recursion
- Groovy collections/streams/closures

It may break due to serialization. Use:

```groovy
@NonCPS
def recursiveHelper(...) {
    ...
}
```

⚠️ `@NonCPS` methods **cannot call pipeline steps** (`sh`, `echo`, etc.)

---

## 🧪 Testing Shared Libraries

### Option 1: JenkinsPipelineUnit (Groovy Unit Tests)
- Simulate pipeline steps
- Test library logic with mocks

### Option 2: Manual Job Testing
- Create a scratch job in Jenkins
- Point it at a branch of your shared library
- Use a test Jenkinsfile to call your functions

---

## 🔐 Authentication

When using a private GitHub repo:

1. Create a **GitHub Personal Access Token (PAT)**
2. Add it to Jenkins Credentials
3. Reference it in your Jenkinsfile or in Jenkins global library config

---

## 💡 Use Shared Libraries For

| Use Case                          | ✅ Good Choice?  |
|----------------------------------|------------------|
| Standardizing build/test/deploy  | ✅ Yes           |
| Avoiding duplication             | ✅ Yes           |
| App-specific deployment logic    | ❌ No            |
| Testing shell scripts or templates | ✅ Yes        |
| Modular DevOps patterns          | ✅ Yes           |

---

## ✅ TL;DR – Final Checklist

| Tip                               | Why It Matters                            |
|----------------------------------|-------------------------------------------|
| Use `vars/` for global steps     | Makes them callable like `buildJar()`     |
| Use `src/` for complex logic     | Keeps code modular & testable             |
| Use version tags (`@lib@v1.0.0`) | Prevents breaking existing pipelines      |
| Add `@NonCPS` if needed          | Solves recursion/stream serialization     |
| Use `libraryResource()` for static files | Great for templates and configs   |
| Centralize only generic logic    | Avoid app-specific code in the library    |

---

## 🧪 Example Project Skeleton

```
project-repo/
├── Jenkinsfile                      # Uses shared library
└── ...
jenkins-shared-library/
├── vars/
│   └── dockerPush.groovy            # Global step
├── src/org/endiesworld/
│   └── DockerHelper.groovy         # Helper class
├── resources/
│   └── scripts/init.sh             # Shell script
└── ...
```

---

Would you like a starter template for `jenkins-shared-library` with working examples of `vars/`, `src/`, and `resources/`?
