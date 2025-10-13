const BASE_URL = "https://sistema-gastos-694972193726.southamerica-east1.run.app";

export async function buscarUltimaConta(userID: string) {
  const resp = await fetch(`${BASE_URL}/usuarios/${userID}/contas`);
  const data = await resp.json();

  if (!resp.ok || !data.objeto?.length) {
    return null; // sem conta ou erro
  }

  // Retorna a última conta do array
  return data.objeto[data.objeto.length - 1];
}
