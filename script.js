function castVote(candidate) {
  const name = document.getElementById("voter").value.trim();
  const response = document.getElementById("response");

  if (name === "") {
    response.innerText = "⚠️ Please enter your name.";
    return;
  }

  fetch("http://localhost:5000/vote", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ voter: name, candidate: candidate })
  })
    .then(res => res.text())
    .then(data => {
      response.innerText = data;
      document.getElementById("voter").value = "";
      fetchResults(); // Refresh results after voting
    })
    .catch(err => {
      response.innerText = "❌ Error connecting to server.";
      console.error(err);
    });
}


function fetchResults() {
  fetch("http://localhost:5000/results")
    .then(res => res.json())
    .then(data => {
      const resultsDiv = document.getElementById("results");
      resultsDiv.innerHTML = "";
      for (const candidate in data) {
        resultsDiv.innerHTML += `<p>${candidate}: ${data[candidate]} votes</p>`;
      }
    })
    .catch(err => {
      document.getElementById("results").innerText = "⚠️ Unable to fetch results.";
      console.error(err);
    });
}


setInterval(fetchResults, 5000);

fetchResults();
