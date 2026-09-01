const listaChapas = document.getElementById("listaChapas");
const modalVoto = document.getElementById("modalVoto");
const fecharModal = document.getElementById("fecharModal");
const formVoto = document.getElementById("formVoto");
const nomeChapaVoto = document.getElementById("nomeChapaVoto");
const raAluno = document.getElementById("raAluno");
const senhaAluno = document.getElementById("senhaAluno");
const mensagemVoto = document.getElementById("mensagemVoto");
const chapas = JSON.parse(localStorage.getItem("chapas")) || [];
let chapaSelecionada = null;
function carregarChapas() {
    listaChapas.innerHTML = "";
    if (chapas.length === 0) {
        listaChapas.innerHTML = "<p>Nenhuma chapa disponível para votação.</p>";
        return;
    }
    chapas.forEach(chapa => {
        const card = document.createElement("div");
        card.classList.add("card-chapa");
        let membrosHTML = "";
        const ordemFuncoes = [
            "Presidente",
            "Vice-Presidente",
            "Secretário",
            "Tesoureiro",
            "Comunicação",
            "Eventos",
            "Esportes",
            "Cultura"
        ];
        const membrosOrdenados = [...chapa.membros].sort((a, b) => {
            return ordemFuncoes.indexOf(a.funcao) - ordemFuncoes.indexOf(b.funcao);
        });
        membrosOrdenados.forEach(membro => {
            membrosHTML += `
                <div class="membro">
                    <strong>${membro.nome}</strong> - ${membro.funcao} - <span>Sala: ${membro.serie}</span>
                </div>
            `;
        });
        card.innerHTML = `
            <h2>${chapa.nome}</h2>
            <p><strong>Chapa número:</strong> ${chapa.numero}</p>
            <p><strong>Objetivos:</strong> ${chapa.objetivos}</p>
            <h3>Membros</h3>
            <div class="lista-membros">
                ${membrosHTML}
            </div>
            <button class="botao-votar">Votar</button>
        `;
        const botaoVotar = card.querySelector(".botao-votar");
        botaoVotar.addEventListener("click", () => {
            chapaSelecionada = chapa;
            nomeChapaVoto.textContent = `Você está votando na ${chapa.nome}`;
            raAluno.value = "";
            senhaAluno.value = "";
            mensagemVoto.textContent = "";
            modalVoto.classList.add("ativo");
            raAluno.focus();
        });
        listaChapas.appendChild(card);
    });
}
fecharModal.addEventListener("click", () => {
    modalVoto.classList.remove("ativo");
});
modalVoto.addEventListener("click", event => {
    if (event.target === modalVoto) {
        modalVoto.classList.remove("ativo");
    }
});
formVoto.addEventListener("submit", event => {
    event.preventDefault();
    const ra = raAluno.value;
    const senha = senhaAluno.value;
    console.log("RA:", ra);
    console.log("Senha:", senha);
    console.log("Chapa escolhida:", chapaSelecionada.nome);
});
carregarChapas();