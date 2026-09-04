const modalMembros = document.getElementById("modalMembros");
const fecharMembros = document.getElementById("fecharMembros");
const nomeChapaMembros = document.getElementById("nomeChapaMembros");
const listaMembros = document.getElementById("listaMembros");
const formMembros = document.getElementById("formMembros");

function abrirGerenciarMembros() {
    const chapa = JSON.parse(localStorage.getItem("chapaEmEdicao"));
    if (!chapa) return;
    nomeChapaMembros.textContent = chapa.nome;
    listaMembros.innerHTML = "";
    chapa.membros.forEach((membro, index) => {
        const div = document.createElement("div");
        div.classList.add("membro-flutuante");
        div.innerHTML = `
            <h3>Membro ${index + 1}</h3>
            <input type="text" class="membro-nome" value="${membro.nome}" placeholder="Nome" required>
            <input type="text" class="membro-ra" value="${membro.ra}" placeholder="RA" required>
            <select class="membro-serie" required>
                <option value="6ºA" ${membro.serie === "6ºA" ? "selected" : ""}>6ºA</option>
                <option value="6ºB" ${membro.serie === "6ºB" ? "selected" : ""}>6ºB</option>
                <option value="7ºA" ${membro.serie === "7ºA" ? "selected" : ""}>7ºA</option>
                <option value="7ºB" ${membro.serie === "7ºB" ? "selected" : ""}>7ºB</option>
                <option value="8ºA" ${membro.serie === "8ºA" ? "selected" : ""}>8ºA</option>
                <option value="8ºB" ${membro.serie === "8ºB" ? "selected" : ""}>8ºB</option>
                <option value="9ºA" ${membro.serie === "9ºA" ? "selected" : ""}>9ºA</option>
                <option value="9ºB" ${membro.serie === "9ºB" ? "selected" : ""}>9ºB</option>
            </select>
            <select class="membro-funcao" required>
                <option value="Presidente" ${membro.funcao === "Presidente" ? "selected" : ""}>Presidente</option>
                <option value="Vice-Presidente" ${membro.funcao === "Vice-Presidente" ? "selected" : ""}>Vice-Presidente</option>
                <option value="Secretário" ${membro.funcao === "Secretário" ? "selected" : ""}>Secretário</option>
                <option value="Tesoureiro" ${membro.funcao === "Tesoureiro" ? "selected" : ""}>Tesoureiro</option>
                <option value="Comunicação" ${membro.funcao === "Comunicação" ? "selected" : ""}>Comunicação</option>
                <option value="Eventos" ${membro.funcao === "Eventos" ? "selected" : ""}>Eventos</option>
                <option value="Esportes" ${membro.funcao === "Esportes" ? "selected" : ""}>Esportes</option>
                <option value="Cultura" ${membro.funcao === "Cultura" ? "selected" : ""}>Cultura</option>
            </select>
        `;
        listaMembros.appendChild(div);
    });
    modalMembros.classList.add("ativo");
}

fecharMembros.addEventListener("click", () => {
    modalMembros.classList.remove("ativo");
});

modalMembros.addEventListener("click", (event) => {
    if (event.target === modalMembros) {
        modalMembros.classList.remove("ativo");
    }
});

formMembros.addEventListener("submit", (event) => {
    event.preventDefault();
    const chapa = JSON.parse(localStorage.getItem("chapaEmEdicao"));
    if (!chapa) return;
    const membrosEditados = [];
    const funcoesUsadas = [];
    const membrosHTML = document.querySelectorAll(".membro-flutuante");
    for (const membro of membrosHTML) {
        const nome = membro.querySelector(".membro-nome").value;
        const ra = membro.querySelector(".membro-ra").value;
        const serie = membro.querySelector(".membro-serie").value;
        const funcao = membro.querySelector(".membro-funcao").value;
        if (funcoesUsadas.includes(funcao)) {
            alert("Não é permitido ter dois membros com a mesma função.");
            return;
        }
        funcoesUsadas.push(funcao);
        membrosEditados.push({
            nome: nome,
            ra: ra,
            serie: serie,
            funcao: funcao
        });
    }
    let chapas = JSON.parse(localStorage.getItem("chapas")) || [];
    const index = chapas.findIndex((item) => item.id === chapa.id);
    if (index === -1) return;
    chapas[index].membros = membrosEditados;
    localStorage.setItem("chapas", JSON.stringify(chapas));
    localStorage.setItem("chapaEmEdicao", JSON.stringify(chapas[index]));
    modalMembros.classList.remove("ativo");
    window.dispatchEvent(new CustomEvent("membrosAtualizados"));
    alert("Membros atualizados com sucesso!");
});