# Contribution Guide

This document outlines the branching strategy for this project and how to contribute changes.

## Branch Purposes

- `{minecraft version}/main`: Stable branch containing production-ready code. All completed work is eventually merged here.
- `{minecraft version}/feat/*`: For developing new features. Create a `{minecraft version}-feat/` branch for each independent feature.
- `{minecraft version}/bugfix/*`: For addressing non-critical bugs. Use when fixing issues found during development or testing.
- `{minecraft version}/hotfix/*`: For urgent fixes to production releases. Use only when a critical issue in `main` must be resolved immediately.
- `{minecraft version}/release`: For preparing a new production release. Use to finalize features and bug fixes before merging to `main`.

## When to Create Branches

| Branch Type | When to Create |
|-------------|----------------|
| `{minecraft version}-feat/*` | Starting work on a new feature or enhancement. |
| `{minecraft version}-bugfix/*`  | Fixing a bug that is not urgent in production. |
| `{minecraft version}-hotfix/*`  | Addressing a critical issue in production that requires immediate action. |
| `{minecraft version}-release` | Coordinating changes for an upcoming release. |

## Merge Best Practices

1. Keep branches focused and up to date with `main`.
2. Open pull requests early for feedback.
3. Ensure all tests and checks pass before merging.
4. Use squash or rebase merges to maintain a clean history, unless project guidelines specify otherwise.
5. Delete branches after merge to keep the repository tidy.

## Further Contribution Guidelines

For more detailed contribution information, refer to general open source guidelines such as [GitHub's guide to contributing](https://docs.github.com/en/get-started/quickstart/contributing-to-projects) and the [Open Source Guides](https://opensource.guide/how-to-contribute/). Follow any organization-wide contribution standards that apply to this project.
