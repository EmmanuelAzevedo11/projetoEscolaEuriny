const slides = document.querySelectorAll(".slide");
const previous = document.getElementById("previous");
const next = document.getElementById("next");

let currentSlide = 0;
let animating = false;

function changeSlide(nextSlide) {
    if (animating || nextSlide === currentSlide) {
        return;
    }

    animating = true;

    const current = slides[currentSlide];
    const next = slides[nextSlide];

    current.classList.remove("active");
    current.classList.add("leaving");

    next.classList.add("active");
    next.classList.add("entering");

    setTimeout(() => {
        current.classList.remove("leaving");
        next.classList.remove("entering");

        currentSlide = nextSlide;
        animating = false;
    }, 800);
}

next.addEventListener("click", () => {
    let nextSlide = currentSlide + 1;

    if (nextSlide >= slides.length) {
        nextSlide = 0;
    }

    changeSlide(nextSlide);
});

previous.addEventListener("click", () => {
    let nextSlide = currentSlide - 1;

    if (nextSlide < 0) {
        nextSlide = slides.length - 1;
    }

    changeSlide(nextSlide);
});

setInterval(() => {
    let nextSlide = currentSlide + 1;

    if (nextSlide >= slides.length) {
        nextSlide = 0;
    }

    changeSlide(nextSlide);
}, 5000);