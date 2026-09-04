const botao = document.getElementById("botao");
const modal = document.getElementById("modalChapa");
const fechar = document.getElementById("fecharModal");
const form = document.getElementById("formChapa");
botao.addEventListener("click", () => {
    modal.classList.add("ativo");
});
fechar.addEventListener("click", () => {
    modal.classList.remove("ativo");
});
function atualizarFuncoes() {
    const selects = document.querySelectorAll(".funcao");
    const selecionados = Array.from(selects)
        .map(select => select.value)
        .filter(value => value !== "");

    selects.forEach(select => {
        const valorAtual = select.value;

        Array.from(select.options).forEach(option => {
            if (option.value === "") return;

            option.disabled =
                selecionados.includes(option.value) &&
                option.value !== valorAtual;
        });
    });
}
document.querySelectorAll(".funcao").forEach(select => {
    select.addEventListener("change", atualizarFuncoes);
});
form.addEventListener("submit", (event) => {
    event.preventDefault();
    const nomeChapa = document.getElementById("nomeChapa").value;
    const numero = document.getElementById("numero").value;
    let chapas = JSON.parse(localStorage.getItem("chapas")) || [];

    if (chapas.some(chapa => Number(chapa.numero) === Number(numero))) {
    alert("Esse número de chapa já está sendo usado.");
    return;
}
    const objetivos = document.getElementById("objetivos").value;
    const senha = document.getElementById("senhaChapa").value;
    const membros = [];
    const funcoesUsadas = [];
    const membrosHTML = document.querySelectorAll(".membro");
    for (const membro of membrosHTML) {
        const inputs = membro.querySelectorAll("input");
        const sala = membro.querySelector(".sala");
        const funcao = membro.querySelector(".funcao");
        if (funcoesUsadas.includes(funcao.value)) {
            alert("Não é permitido ter dois membros com a mesma função.");
            return;
        }
        funcoesUsadas.push(funcao.value);
        membros.push({
            nome: inputs[0].value,
            ra: inputs[1].value,
            serie: sala.value,
            funcao: funcao.value
        });
    }
    const chapa = {
        id: Date.now(),
        nome: nomeChapa,
        numero: numero,
        objetivos: objetivos,
        senha: senha,
        membros: membros
    };
    chapas.push(chapa);
    localStorage.setItem("chapas", JSON.stringify(chapas));
    alert("Chapa criada com sucesso!");
    form.reset();
    modal.classList.remove("ativo");
});