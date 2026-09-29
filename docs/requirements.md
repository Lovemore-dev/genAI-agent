# GenAI Agent — Requirements

## 1. Purpose
A Java-based AI agent that answers questions by reasoning step by step,
calling tools when needed, and retrieving answers from a private
document set (RAG) when the question requires it. Built as a solo
portfolio project to demonstrate practical GenAI/agent/RAG skills for
junior GenAI developer roles.

## 2. Users
- Solo project: built by me, for me, as a portfolio piece and
  interview talking point. No other users planned.

## 3. User Stories
1. As a user, I can ask a factual or reasoning question and get a
   direct answer from the model.
2. As a user, I can ask a question requiring calculation (e.g. "what
   is 17% of 2340") and get a correct, tool-computed answer, not a
   guess from the model.
3. As a user, I can ask what the current time/date is and get a real
   answer via a tool, not a hallucinated one.
4. As a user, I can ask a question about my own document set and get
   an answer grounded in those documents, with the source cited.
   (Document set to be decided in Phase 3.)
5. As a user, if my question isn't answerable from the documents, I'm
   told that rather than given a made-up answer.
6. As a user, if the API is temporarily unavailable (rate limit,
   server overload), the agent retries automatically instead of
   failing on the first hiccup.
7. As a developer, I can see the agent's reasoning trace (Thought /
   Action / Observation) for each step, so I can debug its decisions.
8. As a developer, I can add a new tool without changing the core
   agent loop.

## 4. Functional Requirements
- FR1: The system sends prompts to the Gemini API and returns model text.
- FR2: The system supports defining tools (name, description, input
  schema, execute method).
- FR3: The system runs a Reason-Act-Observe loop: the model can call a
  tool, receive the result, and continue reasoning until it gives a
  final answer.
- FR4: The loop has a maximum iteration cap to prevent infinite loops.
- FR5: The system maintains conversation history within a session.
- FR6: The system chunks a document set, generates embeddings, and
  retrieves the top-k relevant chunks for a query.
- FR7: The system exposes retrieval as a tool the agent can choose to
  call (agentic RAG), rather than always injecting context.
- FR8: The system logs each Thought/Action/Observation step to the console.

## 5. Non-Functional Requirements
- NFR1: API errors are handled: temporary errors (429, 503) retry
  with exponential backoff; permanent errors (400, 403, 404) fail
  immediately with a clear message.
- NFR2: No API key or other secret appears in source code, logs, or
  version control.
- NFR3: The model name is defined in one place (config/constant), not
  hardcoded across files.
- NFR4: Core logic (chunking, similarity, tool registry) has automated
  tests, run independently of the live API.
- NFR5: Setup and usage are documented in the README well enough for
  someone else to run the project from a clean checkout.

## 6. Out of Scope
- No user authentication or multi-user support.
- No persistent database (embeddings cached to disk, not a full DB)
  unless Phase 7 is completed.
- No production deployment/hosting.
- No UI in the first version — console-based only. A UI may be added
  later, once the core engine (agent loop + RAG) is working reliably.

## 7. Acceptance Criteria (samples — expand per user story above)
- US2: Given "What is 17% of 2340?", the agent calls the calculator
  tool and returns 397.8, not a model-guessed number.
- US4: Given a question answerable from the loaded documents, the
  agent's answer matches the source content and names which
  document/chunk it came from.
- US6: Given a simulated 503 response, the agent retries up to the
  configured max attempts before failing, with visible backoff delays
  in the log.