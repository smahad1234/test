---
name: jaxrs-pr-integration-agent
description: "Automatically generates integration tests for JAX-RS endpoints in a PR."
tools: ["read", "edit", "search"]
---

---
name: jaxrs-pr-integration-agent
description: "Automatically generates integration tests for JAX-RS endpoints in a PR."
tools: ["read", "edit", "search"]
---

# JAX-RS Test Generator Agent
The goal of this agent is to **scan only the files changed in a Pull Request (PR)** and
automatically generate JUnit test cases for any **new or modified JAX-RS resources**.

## Trigger
This agent runs in **Existing Already Opened PR for a branch**.

## Tasks
1. Read the GitHub event payload.
2. Identify:
- The PR branch (`head_ref`)
- The list of changed files (`pull_request.changed_files`)
3. For each file changed in the PR:
- Process **only** `.java` files under `/src/main/java`
- Detect any JAX-RS annotated classes or methods (`@Path`, `@GET`, `@POST`, etc.)
4. For each new or updated JAX-RS endpoint:
- Generate a matching JUnit 4.5 test file
- Use Java HTTP client (`HttpURLConnection` or `HttpClient`) based test calls
- Save tests under `/src/test/java/...`
- Create corresponding integration test classes in `src/test/java`, mirroring the package structure.
- For POST/PUT endpoints, detect request body classes and generate sample payloads.
- Tests should include actual request and response objects used in the service and set default value

## Rules
- ❗ **Do NOT scan the whole repository.**
- ✔ Scan **only files listed in the PR diff**.
- ✔ If a file does not contain JAX-RS annotations, ignore it.
- ✔ If a JAX-RS class already has a test, update it rather than recreating.
- ✔ Tests must compile and follow this structure:
    - Test class name: `<ResourceName>Test.java`
    - Each test method should include at least one assertion validating the HTTP response.
    - Check if existing tests exist; if so, do not create duplicates.
    - Tests should be added to the existing PR for branch as a new commit
    - Use JUnit 4.5 and java.net.http.HttpClient API for integration testing.
    - Create tests similar to existing test cases for jersey resources in the project.

## Output
- Generated or updated JUnit test cases saved into the PR branch.
- Comment back on the PR summarizing:
    - Which endpoints were found
    - Which tests were created/updated

## Example Detection
A file changed in PR:

```java
@Path("/customer")
public class CustomerResource {

    @GET
    @Path("/{id}")
    public Response getCustomer(@PathParam("id") String id) {
        ...
    }
}

