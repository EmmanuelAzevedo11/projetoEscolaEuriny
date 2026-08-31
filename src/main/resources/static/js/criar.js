const botao = document.getElementById("botao");
const modal = document.getElementById("modalChapa");
const fechar = document.getElementById("fecharModal");

botao.addEventListener("click", () => {
    modal.classList.add("ativo");
});

fechar.addEventListener("click", () => {
    modal.classList.remove("ativo");
});