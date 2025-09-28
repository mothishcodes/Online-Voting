document.getElementById('voteForm').addEventListener('submit', async function (e) {
e.preventDefault();
const voter_id = document.getElementById('voter_id').value.trim();
const candidate = document.getElementById('candidate').value;
const msg = document.getElementById('message');
msg.textContent = '';


try {
const formData = new URLSearchParams();
formData.append('voter_id', voter_id);
formData.append('candidate', candidate);


const res = await fetch('/VoteHandler', {
method: 'POST',
body: formData
});


const data = await res.json();
if (res.ok) {
msg.style.color = 'green';
msg.textContent = data.message || 'Vote recorded';
} else {
msg.style.color = 'darkred';
msg.textContent = data.message || 'Error';
}
} catch (err) {
msg.style.color = 'darkred';
msg.textContent = 'Network error';
console.error(err);
}
});
