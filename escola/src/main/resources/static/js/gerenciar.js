const listaChapas = document.getElementById("listaChapas");
const modalSenha = document.getElementById("modalSenha");
const fecharModal = document.getElementById("fecharModal");
const formSenha = document.getElementById("formSenha");
const nomeChapaModal = document.getElementById("nomeChapaModal");
const senha = document.getElementById("senha");
const mensagemSenha = document.getElementById("mensagemSenha");
const modalEdicao = document.getElementById("modalEdicao");
const fecharEdicao = document.getElementById("fecharEdicao");
const formEdicao = document.getElementById("formEdicao");
const editarNome = document.getElementById("editarNome");
const editarObjetivos = document.getElementById("editarObjetivos");
const gerenciarMembros = document.getElementById("gerenciarMembros");
const apagarChapa = document.getElementById("apagarChapa");
let chapaSelecionada = null;

function carregarChapas() {
    listaChapas.innerHTML = "";

    const chapas = JSON.parse(localStorage.getItem("chapas")) || [];

    if (chapas.length === 0) {
        listaChapas.innerHTML = "<p>Nenhuma chapa cadastrada.</p>";
        return;
    }

    chapas.forEach((chapa) => {
        const div = document.createElement("div");

        div.classList.add("card-chapa");

        div.innerHTML = `
            <h2>${chapa.nome}</h2>
            <p>Número: ${chapa.numero}</p>
            <button class="gerenciar" data-id="${chapa.id}">Gerenciar</button>
        `;

        listaChapas.appendChild(div);

        div.addEventListener("click", () => {
            const id = Number(chapa.id);

            const chapasAtualizadas = JSON.parse(localStorage.getItem("chapas")) || [];

            chapaSelecionada = chapasAtualizadas.find(
                (chapa) => chapa.id === id
            );

            if (!chapaSelecionada) return;

            nomeChapaModal.textContent = chapaSelecionada.nome;

            senha.value = "";
            mensagemSenha.textContent = "";

            modalSenha.classList.add("ativo");
        });
    });
}
fecharModal.addEventListener("click", () => {
    modalSenha.classList.remove("ativo");
});

modalSenha.addEventListener("click", (event) => {
    if (event.target === modalSenha) {
        modalSenha.classList.remove("ativo");
    }
});

formSenha.addEventListener("submit", (event) => {
    event.preventDefault();
    if (!chapaSelecionada) return;
    if (senha.value === chapaSelecionada.senha) {
        modalSenha.classList.remove("ativo");
        editarNome.value = chapaSelecionada.nome;
        editarObjetivos.value = chapaSelecionada.objetivos;
        localStorage.setItem("chapaEmEdicao", JSON.stringify(chapaSelecionada));
        modalEdicao.classList.add("ativo");
    } else {
        mensagemSenha.textContent = "Senha incorreta!";
    }
});

fecharEdicao.addEventListener("click", () => {
    modalEdicao.classList.remove("ativo");
});

modalEdicao.addEventListener("click", (event) => {
    if (event.target === modalEdicao) {
        modalEdicao.classList.remove("ativo");
    }
});

formEdicao.addEventListener("submit", (event) => {
    event.preventDefault();
    if (!chapaSelecionada) return;
    let chapas = JSON.parse(localStorage.getItem("chapas")) || [];
    const index = chapas.findIndex((chapa) => chapa.id === chapaSelecionada.id);
    if (index === -1) return;
    chapas[index].nome = editarNome.value;
    chapas[index].objetivos = editarObjetivos.value;
    chapaSelecionada = chapas[index];
    localStorage.setItem("chapas", JSON.stringify(chapas));
    localStorage.setItem("chapaEmEdicao", JSON.stringify(chapaSelecionada));
    carregarChapas();
    alert("Chapa atualizada com sucesso!");
});

gerenciarMembros.addEventListener("click", () => {
    if (!chapaSelecionada) return;
    localStorage.setItem("chapaEmEdicao", JSON.stringify(chapaSelecionada));
    abrirGerenciarMembros();
});

apagarChapa.addEventListener("click", () => {
    if (!chapaSelecionada) return;
    const confirmar = confirm(`Tem certeza que deseja apagar a chapa "${chapaSelecionada.nome}"?`);
    if (!confirmar) return;
    let chapas = JSON.parse(localStorage.getItem("chapas")) || [];
    chapas = chapas.filter((chapa) => chapa.id !== chapaSelecionada.id);
    localStorage.setItem("chapas", JSON.stringify(chapas));
    localStorage.removeItem("chapaEmEdicao");
    chapaSelecionada = null;
    modalEdicao.classList.remove("ativo");
    carregarChapas();
    alert("Chapa apagada com sucesso!");
});

window.addEventListener("membrosAtualizados", () => {
    const chapaAtualizada = JSON.parse(localStorage.getItem("chapaEmEdicao"));
    if (chapaAtualizada) {
        chapaSelecionada = chapaAtualizada;
    }
});

carregarChapas();