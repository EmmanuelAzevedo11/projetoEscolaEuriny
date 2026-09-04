const listaChapas = document.getElementById("listaChapas");
const modal = document.getElementById("modalSenha");
const fecharModal = document.getElementById("fecharModal");
const formSenha = document.getElementById("formSenha");
const nomeChapaModal = document.getElementById("nomeChapaModal");
const mensagemSenha = document.getElementById("mensagemSenha");
const campoSenha = document.getElementById("senha");
const modalEdicao = document.getElementById("modalEdicao");
const fecharEdicao = document.getElementById("fecharEdicao");
const formEdicao = document.getElementById("formEdicao");
const editarNome = document.getElementById("editarNome");
const editarObjetivos = document.getElementById("editarObjetivos");
const editarMembros = document.getElementById("editarMembros");
const apagarChapa = document.getElementById("apagarChapa");
const chapas = JSON.parse(localStorage.getItem("chapas")) || [];
let chapaSelecionada = null;
chapas.forEach((chapa) => {
    const card = document.createElement("div");
    card.classList.add("card-chapa");
    card.innerHTML = `
        <h2>${chapa.nome}</h2>
        <p>Chapa número ${chapa.numero}</p>
    `;
    card.addEventListener("click", () => {
        chapaSelecionada = chapa;
        nomeChapaModal.textContent = chapa.nome;
        campoSenha.value = "";
        mensagemSenha.textContent = "";
        modal.classList.add("ativo");
        campoSenha.focus();
    });
    listaChapas.appendChild(card);
});
fecharModal.addEventListener("click", () => {
    modal.classList.remove("ativo");
});
modal.addEventListener("click", (event) => {
    if (event.target === modal) {
        modal.classList.remove("ativo");
    }
});
formSenha.addEventListener("submit", (event) => {
    event.preventDefault();
    const senhaDigitada = campoSenha.value;
    if (senhaDigitada === chapaSelecionada.senha) {
        modal.classList.remove("ativo");
        abrirEdicao();
    } else {
        mensagemSenha.style.color = "red";
        mensagemSenha.textContent = "Senha incorreta.";
    }
});
function atualizarFuncoes() {
    const selects = document.querySelectorAll(".editar-funcao");
    const funcoesSelecionadas = Array.from(selects)
        .map(select => select.value)
        .filter(value => value !== "");
    selects.forEach(select => {
        const valorAtual = select.value;
        Array.from(select.options).forEach(option => {
            if (option.value === "") return;
            option.disabled = funcoesSelecionadas.includes(option.value) && option.value !== valorAtual;
        });
    });
}
function abrirEdicao() {
    editarNome.value = chapaSelecionada.nome;
    editarObjetivos.value = chapaSelecionada.objetivos;
    editarMembros.innerHTML = "";
    chapaSelecionada.membros.forEach((membro, index) => {
        const div = document.createElement("div");
        div.classList.add("membro-edicao");
        div.innerHTML = `
            <h4>Membro ${index + 1}</h4>
            <input type="text" class="editar-nome" value="${membro.nome}" placeholder="Nome" required>
            <input type="text" class="editar-ra" value="${membro.ra}" placeholder="RA" required>

            <select class="editar-serie" required>
            <option value="" disabled>Selecione a sala</option>
            <option value="6ºA" ${membro.serie === "6ºA" ? "selected" : ""}>6ºA</option>
            <option value="6ºB" ${membro.serie === "6ºB" ? "selected" : ""}>6ºB</option>
            <option value="7ºA" ${membro.serie === "7ºA" ? "selected" : ""}>7ºA</option>
            <option value="7ºB" ${membro.serie === "7ºB" ? "selected" : ""}>7ºB</option>
            <option value="8ºA" ${membro.serie === "8ºA" ? "selected" : ""}>8ºA</option>
            <option value="8ºB" ${membro.serie === "8ºB" ? "selected" : ""}>8ºB</option>
            <option value="9ºA" ${membro.serie === "9ºA" ? "selected" : ""}>9ºA</option>
            <option value="9ºB" ${membro.serie === "9ºB" ? "selected" : ""}>9ºB</option>
            </select>

            <select class="editar-funcao" required>
                <option value="" disabled>Selecione a função</option>
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
        editarMembros.appendChild(div);
        const selectFuncao = div.querySelector(".editar-funcao");
        selectFuncao.addEventListener("change", atualizarFuncoes);
    });
    atualizarFuncoes();
    modalEdicao.classList.add("ativo");
}
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
    chapaSelecionada.nome = editarNome.value;
    chapaSelecionada.objetivos = editarObjetivos.value;
    const membrosEditados = [];
    const funcoesUsadas = [];
    const membrosHTML = document.querySelectorAll(".membro-edicao");
    for (const membro of membrosHTML) {
        const nome = membro.querySelector(".editar-nome").value;
        const ra = membro.querySelector(".editar-ra").value;
        const serie = membro.querySelector(".editar-serie").value;
        const funcao = membro.querySelector(".editar-funcao").value;
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
    chapaSelecionada.membros = membrosEditados;
    localStorage.setItem("chapas", JSON.stringify(chapas));
    modalEdicao.classList.remove("ativo");
    atualizarCards();
    alert("Alterações salvas com sucesso!");
});
apagarChapa.addEventListener("click", () => {
    const confirmar = confirm(`Tem certeza que deseja apagar a chapa "${chapaSelecionada.nome}"?`);
    if (!confirmar) {
        return;
    }
    const indice = chapas.findIndex((chapa) => chapa.id === chapaSelecionada.id);
    if (indice !== -1) {
        chapas.splice(indice, 1);
    }
    localStorage.setItem("chapas", JSON.stringify(chapas));
    modalEdicao.classList.remove("ativo");
    chapaSelecionada = null;
    atualizarCards();
    alert("Chapa apagada com sucesso!");
});
function atualizarCards() {
    listaChapas.innerHTML = "";
    chapas.forEach((chapa) => {
        const card = document.createElement("div");
        card.classList.add("card-chapa");
        card.innerHTML = `
            <h2>${chapa.nome}</h2>
            <p>Chapa número ${chapa.numero}</p>
        `;
        card.addEventListener("click", () => {
            chapaSelecionada = chapa;
            nomeChapaModal.textContent = chapa.nome;
            campoSenha.value = "";
            mensagemSenha.textContent = "";
            modal.classList.add("ativo");
            campoSenha.focus();
        });
        listaChapas.appendChild(card);
    });
}