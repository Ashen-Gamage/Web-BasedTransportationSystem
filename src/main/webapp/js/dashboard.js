document.addEventListener('DOMContentLoaded', function() {
    const links = document.querySelectorAll('.sidebar a');
    const content = document.getElementById('dashboard-content');

    links.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();

            const page = this.getAttribute('data-page'); // e.g., user/profile
            const url = `../${page}.jsp`;

            fetch(url)
                .then(response => {
                    if (!response.ok) throw new Error('Page not found');
                    return response.text();
                })
                .then(html => {
                    content.innerHTML = html;
                })
                .catch(err => {
                    content.innerHTML = `<p style="color:red;">Error loading page: ${err.message}</p>`;
                });
        });
    });
});
