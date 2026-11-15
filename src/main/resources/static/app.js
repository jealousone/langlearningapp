const API = "/api";
const view = document.getElementById("app-view");
let currentLesson = null;
let currentIndex = 0;
let answers = {};
let selectedOption = null;

async function request(url, options = {}) {
    const response = await fetch(API + url, {
        headers: { "Content-Type": "application/json" },
        ...options
    });
    if (!response.ok) {
        const message = await response.text();
        throw new Error(message || "Something went wrong.");
    }
    return response.json();
}

function toast(message) {
    const el = document.getElementById("toast");
    el.textContent = message;
    el.classList.add("show");
    setTimeout(() => el.classList.remove("show"), 2400);
}

function setPageTitle(title) {
    document.getElementById("page-title").innerHTML = title;
}

function setActive(page) {
    document.querySelectorAll(".nav-link").forEach(button => {
        button.classList.toggle("active", button.dataset.page === page);
    });
}

async function showHome() {
    setActive("home");
    setPageTitle('Good afternoon! <span>✦</span>');
    const [lessons, progress] = await Promise.all([
        request("/lessons"), request("/progress")
    ]);
    view.innerHTML = `
        <div class="hero">
            <div class="hero-copy">
                <div class="hero-tag">YOUR NEXT CHAPTER STARTS HERE</div>
                <h2>Small steps. Big progress.</h2>
                <p>Build a language-learning habit and discover a new word every day.</p>
                <button class="btn" id="start-learning">Continue learning&nbsp; →</button>
            </div>
            <div class="hero-art">🌱</div>
        </div>
        <div class="stats-grid">
            <div class="stat-card"><div class="stat-top">TOTAL POINTS <span class="stat-icon">✦</span></div><div class="stat-number">${progress.totalPoints}</div><div class="stat-caption">Points earned so far</div></div>
            <div class="stat-card"><div class="stat-top">EXERCISES DONE <span class="stat-icon">✓</span></div><div class="stat-number">${progress.totalCompleted}</div><div class="stat-caption">${progress.totalCorrect} correct answers</div></div>
            <div class="stat-card"><div class="stat-top">LESSONS FINISHED <span class="stat-icon">▤</span></div><div class="stat-number">${progress.completedLessons}/${progress.totalLessons}</div><div class="stat-caption">Keep your momentum going</div></div>
        </div>
        <div class="section-head"><h2>Pick up where you left off</h2><button class="text-button" id="all-lessons">VIEW ALL →</button></div>
        <div class="lesson-grid">${lessons.map(lessonCard).join("")}</div>`;
    document.getElementById("start-learning").onclick = () => openLesson(lessons[0].id);
    document.getElementById("all-lessons").onclick = showLessons;
    attachLessonButtons();
}

function lessonCard(lesson) {
    return `<article class="lesson-card">
        <div class="lesson-icon">${lesson.id === "lesson1" ? "🗣️" : "📚"}</div>
        <span class="tag">${lesson.language}</span>
        <h3>${escapeHtml(lesson.title)}</h3>
        <p>${escapeHtml(lesson.description)}</p>
        <div class="lesson-meta"><span>${lesson.exerciseCount} exercises</span>
        <button class="btn secondary" data-lesson="${lesson.id}">${lesson.completed ? "Practice again" : "Start lesson"} →</button></div>
    </article>`;
}

function attachLessonButtons() {
    document.querySelectorAll("[data-lesson]").forEach(button => {
        button.onclick = () => openLesson(button.dataset.lesson);
    });
}

async function showLessons() {
    setActive("lessons");
    setPageTitle("Your lessons <span>✦</span>");
    const lessons = await request("/lessons");
    view.innerHTML = `<p class="page-intro">Choose a lesson and learn at your own pace.</p>
        <div class="lesson-grid">${lessons.map(lessonCard).join("")}</div>`;
    attachLessonButtons();
}

async function openLesson(id) {
    currentLesson = await request(`/lessons/${id}`);
    currentIndex = 0;
    answers = {};
    selectedOption = null;
    setActive("lessons");
    renderExercise();
}

function renderExercise() {
    const exercise = currentLesson.exercises[currentIndex];
    const total = currentLesson.exercises.length;
    setPageTitle(escapeHtml(currentLesson.title));
    const percent = (currentIndex / total) * 100;
    const isChoice = exercise.options && exercise.options.length > 0;
    view.innerHTML = `
        <div class="exercise-wrap">
            <div class="exercise-top"><span>LESSON ${currentIndex + 1} OF ${total}</span><span>${exercise.points} POINTS</span></div>
            <div class="progress-track"><div class="progress-fill" style="width:${percent}%"></div></div>
            <div class="question-card">
                <div class="question-type">${exercise.type.replaceAll("_", " ")}</div>
                <h2>${escapeHtml(exercise.question)}</h2>
                ${isChoice
                    ? `<div class="options">${exercise.options.map(option =>
                        `<button class="option ${selectedOption === option ? "selected" : ""}" data-option="${escapeHtml(option)}">${escapeHtml(option)}</button>`
                      ).join("")}</div>`
                    : `<input class="answer-input" id="answer-input" placeholder="Type your answer here..." autocomplete="off" value="${escapeHtml(answers[exercise.id] || "")}">`}
                <div class="hint" id="hint-area"></div>
                <div class="exercise-actions">
                    <button class="text-button" id="hint-button">💡 Show hint</button>
                    <button class="btn dark" id="check-button">Check answer →</button>
                </div>
                <div id="feedback" class="feedback"></div>
            </div>
        </div>`;
    document.querySelectorAll("[data-option]").forEach(button => {
        button.onclick = () => {
            selectedOption = button.dataset.option;
            document.querySelectorAll("[data-option]").forEach(b => b.classList.toggle("selected", b === button));
        };
    });
    document.getElementById("hint-button").onclick = () => {
        document.getElementById("hint-area").textContent = exercise.hint;
    };
    document.getElementById("check-button").onclick = checkCurrentAnswer;
    const input = document.getElementById("answer-input");
    if (input) input.addEventListener("keydown", event => {
        if (event.key === "Enter") checkCurrentAnswer();
    });
}

function checkCurrentAnswer() {
    const exercise = currentLesson.exercises[currentIndex];
    const input = document.getElementById("answer-input");
    const answer = exercise.options.length ? selectedOption : input.value.trim();
    if (!answer) {
        toast("Choose or type an answer first.");
        return;
    }
    answers[exercise.id] = answer;
    if (currentIndex < currentLesson.exercises.length - 1) {
        currentIndex++;
        selectedOption = null;
        renderExercise();
    } else {
        finishLesson();
    }
}

async function finishLesson() {
    const result = await request(`/lessons/${currentLesson.id}/complete`, {
        method: "POST",
        body: JSON.stringify({ answers })
    });
    renderResults(result);
}

function renderResults(result) {
    setPageTitle("Lesson complete <span>✦</span>");
    const percent = Math.round((result.correctCount / result.totalExercises) * 100);
    view.innerHTML = `
        <div class="exercise-wrap">
            <div class="result-card">
                <div class="result-emoji">${percent >= 60 ? "🎉" : "🌱"}</div>
                <div class="eyebrow">YOU MADE PROGRESS</div>
                <h2>${percent >= 60 ? "Great work!" : "Good effort! Keep practicing."}</h2>
                <div class="result-score">+${result.score} pts</div>
                <p class="page-intro">${result.correctCount} out of ${result.totalExercises} answers correct</p>
                <div class="result-list">${result.results.map(item => `
                    <div class="result-row">
                        <span>${item.correct ? "✓" : "✕"} &nbsp; ${escapeHtml(item.exerciseId)}</span>
                        <span>${item.correct ? `+${item.earnedPoints} points` : `Correct: ${escapeHtml(item.correctAnswer)}`}</span>
                    </div>`).join("")}</div>
                <button class="btn dark" id="back-dashboard">Back to dashboard →</button>
                <button class="btn secondary" id="retry-lesson">Practice again</button>
            </div>
        </div>`;
    document.getElementById("back-dashboard").onclick = showHome;
    document.getElementById("retry-lesson").onclick = () => openLesson(currentLesson.id);
}

async function showProgress() {
    setActive("progress");
    setPageTitle("Your progress <span>✦</span>");
    const progress = await request("/progress");
    view.innerHTML = `
        <p class="page-intro">Every exercise is a step forward. Look how far you've come.</p>
        <div class="stats-grid">
            <div class="stat-card"><div class="stat-top">TOTAL POINTS</div><div class="stat-number">${progress.totalPoints}</div><div class="stat-caption">Your learning score</div></div>
            <div class="stat-card"><div class="stat-top">COMPLETED EXERCISES</div><div class="stat-number">${progress.totalCompleted}</div><div class="stat-caption">${progress.totalCorrect} correct</div></div>
            <div class="stat-card"><div class="stat-top">LESSONS COMPLETED</div><div class="stat-number">${progress.completedLessons}/${progress.totalLessons}</div><div class="stat-caption">Lessons finished</div></div>
        </div>
        <div class="section-head"><h2>Your achievements</h2></div>
        <div class="panel"><div class="achievement-list">
            ${progress.achievements.length
                ? progress.achievements.map((a, i) => `<div class="achievement"><div class="achievement-icon">${["🏅","⭐","🌟"][i % 3]}</div><strong>${escapeHtml(a)}</strong></div>`).join("")
                : `<p class="page-intro">Complete an exercise to unlock your first achievement.</p>`}
        </div></div>`;
}

async function showSettings() {
    setActive("settings");
    setPageTitle("Settings <span>✦</span>");
    const settings = await request("/settings");
    view.innerHTML = `
        <p class="page-intro">Adjust your learning experience.</p>
        <div class="panel">
            <div class="settings-row"><div><strong>Learning language</strong><p>Choose the language you are currently studying.</p></div>
                <select id="language-select">
                    ${["Spanish","English","German","French","Italian"].map(language =>
                        `<option ${settings.language === language ? "selected" : ""}>${language}</option>`).join("")}
                </select>
            </div>
            <div class="settings-row"><div><strong>Learning mode</strong><p>Practice with short lessons and quick exercises.</p></div><span class="tag">Beginner</span></div>
        </div>`;
    document.getElementById("language-select").onchange = async event => {
        await request("/settings/language", {
            method: "PUT",
            body: JSON.stringify({ language: event.target.value })
        });
        toast("Learning language updated.");
    };
}

function escapeHtml(value) {
    return String(value ?? "").replace(/[&<>"']/g, char => ({
        "&":"&amp;", "<":"&lt;", ">":"&gt;", '"':"&quot;", "'":"&#39;"
    }[char]));
}

document.querySelectorAll(".nav-link").forEach(button => {
    button.addEventListener("click", async () => {
        try {
            if (button.dataset.page === "home") await showHome();
            if (button.dataset.page === "lessons") await showLessons();
            if (button.dataset.page === "progress") await showProgress();
            if (button.dataset.page === "settings") await showSettings();
        } catch (error) {
            view.innerHTML = `<div class="panel">Could not load this page: ${escapeHtml(error.message)}</div>`;
        }
    });
});

showHome().catch(error => {
    view.innerHTML = `<div class="panel">Could not connect to the server. Make sure the Spring Boot application is running.<br><br>${escapeHtml(error.message)}</div>`;
});
