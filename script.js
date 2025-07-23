function castVote(candidate) {
  const name = document.getElementById("voter").value.trim();
  const response = document.getElementById("response");

  if (name === "") {
    response.innerText = "⚠️ Please enter your name.";
    return;
  }

  const xhr = new XMLHttpRequest();
  xhr.open("POST", "http://localhost:5000/vote", true);
  xhr.setRequestHeader("Content-Type", "application/json");

  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      response.innerText = xhr.responseText;
      document.getElementById("voter").value = "";
    }
  };

  const data = {
    voter: name,
    candidate: candidate
  };

  xhr.send(JSON.stringify(data));
}
